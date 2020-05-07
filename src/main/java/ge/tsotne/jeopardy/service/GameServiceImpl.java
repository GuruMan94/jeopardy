package ge.tsotne.jeopardy.service;

import ge.tsotne.jeopardy.Utils;
import ge.tsotne.jeopardy.configuration.UserPrincipal;
import ge.tsotne.jeopardy.model.Game;
import ge.tsotne.jeopardy.model.Game_;
import ge.tsotne.jeopardy.model.Player;
import ge.tsotne.jeopardy.model.dto.game.EnterGameDTO;
import ge.tsotne.jeopardy.model.dto.game.GameSearchDTO;
import ge.tsotne.jeopardy.model.dto.game.scheduler.GameDTO;
import ge.tsotne.jeopardy.repository.GameRepository;
import ge.tsotne.jeopardy.repository.PlayerRepository;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static ge.tsotne.jeopardy.model.Player.Role.PLAYER;
import static ge.tsotne.jeopardy.model.Player.Role.SHOWMAN;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final QuestionPackService questionPackService;
    private final PlayerRepository playerRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private static final ConcurrentHashMap<Long, GameDTO> activeGames = new ConcurrentHashMap<>();

    public GameServiceImpl(GameRepository gameRepository,
                           QuestionPackService questionPackService,
                           PlayerRepository playerRepository, SimpMessagingTemplate messagingTemplate) {
        this.gameRepository = gameRepository;
        this.questionPackService = questionPackService;
        this.playerRepository = playerRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public List<Game> search(GameSearchDTO dto) {
        return gameRepository.findAll(((root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if (!StringUtils.isEmpty(dto.getName())) {
                p = cb.and(p, cb.like(root.get(Game_.name), "%" + dto.getName() + "%"));
            }
            if (dto.getPrivateGame() != null) {
                p = cb.and(p, cb.equal(root.get(Game_.privateGame), dto.getPrivateGame()));
            }
            return p;
        }), Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Override
    public Game get(long id) {
        if (isNotMember(id)) {
            throw new RuntimeException("NOT_ALLOWED");
        }
        return gameRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GAME_NOT_FOUND"));
    }

    @Override
    public Game getActive() {
        return gameRepository.findByPlayerId(Utils.getCurrentUserIdNotNull())
                .orElse(null);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Game create(ge.tsotne.jeopardy.model.dto.game.GameDTO dto) {
        if (exists(dto.getName())) {
            throw new RuntimeException("GAME_ALREADY_EXISTS_WITH_THIS_NAME");
        }
        if (!questionPackService.exists(dto.getQuestionPackId())) {
            throw new RuntimeException("QUESTION_PACK_ID_NOT_FOUND");
        }
        Game game = gameRepository.save(new Game(dto));
        Player player = savePlayer(game.getId(), dto.getRole());
        game.getPlayers().add(player);
        return game;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void enter(long id, EnterGameDTO dto) {
        Game game = get(id);
        validate(game, dto);
        savePlayer(id, dto.getRole());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void start(long id) {
        Game game = get(id);
        if (isNotShowMan(id)) {
            throw new RuntimeException("NOT_ALLOWED");
        }
        if (game.getStatus() != Game.Status.NEW) {
            throw new RuntimeException("CANT_START_GAME");
        }
        game.start();
        gameRepository.save(game);
        addToActiveGames(game);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void end(long id) {
        if (isNotShowMan(id)) {
            throw new RuntimeException("NOT_ALLOWED");
        }
        Game game = get(id);
        game.end();
        gameRepository.save(game);
    }

    @Override
    public void answer(long id) {
        if (isNotPlayer(id)) {
            throw new RuntimeException("NOT_ALLOWED");
        }
        GameDTO game = activeGames.get(id);
        long userId = Utils.getCurrentUserIdNotNull();
        if (game.isPaused() && !game.canAnswer(userId)) return;
        game.setPaused(true);
        game.getAnsweredUsers().add(new GameDTO.User(userId, true));
    }

    @Override
    public void checkAnswer(long id, Boolean correct) {
        if (isNotShowMan(id)) {
            throw new RuntimeException("NOT_ALLOWED");
        }
        GameDTO game = activeGames.get(id);
        int point = game.getQuestionInfo().getCost();
        boolean isCorrect = BooleanUtils.isTrue(correct);
        if (!isCorrect) {
            point *= -1;
        }
        GameDTO.Player player = game.getPlayerByUserId(Utils.getCurrentUserIdNotNull());
        player.addPoint(point);
        if (isCorrect) {
            GameDTO.Theme theme = game.getCurrentTheme();
            if (theme != null) {
                GameDTO.Theme.Question question = theme.getCurrentQuestion();
                if (question != null) {
                    question.setLastIndex(question.getText().length);
                }
            }
        } else {
            game.setPausedUntil(LocalDateTime.now().plusSeconds(5));
        }
        //TODO ლოგირება
    }

    @Override
    public void pause(long id) {
        if (isNotMember(id)) {
            throw new RuntimeException("NOT_ALLOWED");
        }
        GameDTO game = activeGames.get(id);
        if (game == null) return;
        long seconds = LocalDateTime.now().until(game.getPausedUntil(), ChronoUnit.SECONDS);
        game.setSavedPausedSeconds(Math.max(seconds, 0));
        game.setPaused(true);
    }

    @Override
    public void resume(long id) {
        if (isNotMember(id)) {
            throw new RuntimeException("NOT_ALLOWED");
        }
        GameDTO game = activeGames.get(id);
        if (game == null) return;
        game.setPaused(false);
        game.setPausedUntil(LocalDateTime.now().plusSeconds(game.getSavedPausedSeconds()));
    }

    public ConcurrentHashMap<Long, GameDTO> getActiveGames() {
        return activeGames;
    }

    private boolean isNotShowMan(long gameId) {
        long userId = Utils.getCurrentUserIdNotNull();
        return playerRepository.countByGameIdAndUserIdAndRoleAndActiveTrue(gameId, userId, SHOWMAN) == 0;
    }

    private boolean isNotPlayer(long gameId) {
        long userId = Utils.getCurrentUserIdNotNull();
        return playerRepository.countByGameIdAndUserIdAndRoleAndActiveTrue(gameId, userId, PLAYER) == 0;
    }

    private boolean isNotMember(long gameId) {
        long userId = Utils.getCurrentUserIdNotNull();
        return playerRepository.countByGameIdAndUserIdAndActiveTrue(gameId, userId) <= 0;
    }

    private void validate(Game game, EnterGameDTO dto) {
        if (game.getPrivateGame()) {
            validatePassword(game, dto);
        }
        if (game.getStatus() == Game.Status.FINISHED) {
            throw new RuntimeException("CANT_ENTER_GAME");
        }
        //TODO if already in another game
        boolean exists = game.getPlayers()
                .stream().anyMatch(p -> p.getUserId().equals(Utils.getCurrentUserIdNotNull()));
        if (exists) {
            throw new RuntimeException("PLAYER_ALREADY_IN_GAME");
        }

        if (dto.getRole() == SHOWMAN) {
            boolean match = game.getPlayers()
                    .stream().anyMatch(p -> p.getRole().equals(SHOWMAN));
            if (match) {
                throw new RuntimeException("CANT_ENTER_AS_SHOWMAN");
            }
        } else if (dto.getRole() == Player.Role.PLAYER) {
            long count = game.getPlayers()
                    .stream().filter(p -> p.getRole().equals(Player.Role.PLAYER)).count();
            if (count == game.getMaxPlayerCount()) {
                throw new RuntimeException("CANT_ENTER_AS_PLAYER");
            }
        }
    }

    private void validatePassword(Game game, EnterGameDTO dto) {
        if (StringUtils.isEmpty(game.getPassword())
                || StringUtils.isEmpty(dto.getPassword())
                || !game.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("INCORRECT_PASSWORD");
        }
    }

    public boolean exists(String name) {
        return gameRepository.countByNameAndStatusIn(name, List.of(Game.Status.NEW, Game.Status.STARTED)) > 0;
    }

    private Player savePlayer(long gameId, Player.Role role) {
        @NotNull UserPrincipal user = Utils.getCurrentUserNotNull();
        Player player = new Player(gameId, role, user);
        return playerRepository.saveAndFlush(player);
    }

    private void addToActiveGames(Game game) {
        activeGames.put(game.getId(), new GameDTO(game));
    }

    @Async
    public void sendQuestionChunk(GameDTO g) {
        if (g.isPaused() || LocalDateTime.now().compareTo(g.getPausedUntil()) < 0) {
            System.out.println("...");
            return;
        }
        GameDTO.Theme theme = g.getCurrentTheme();
        if (theme == null || g.isFinished()) {
            endGame(g);
            return;
        }
        if (!theme.isNameSent()) {
            startTheme(g, theme);
        } else {
            GameDTO.Theme.Question question = theme.getCurrentQuestion();
            if (question == null) {
                g.incrementThemeIndex();
                return;
            }
            if (!question.isCostSent()) {
                questionStart(g, question);
            } else {
                String message = question.getCurrentChunk();
                if (message == null) {
                    theme.incrementQuestionIndex();
                    questionEnd(g, question);
                    return;
                }
                sentQuestionChunk(g, question, message);
                if (question.isFirstChunk()) {
                    prepareForAnswer(g, question);
                } else if (question.isLastChunk()) {
                    theme.incrementQuestionIndex();
                    if (theme.isLastQuestion()) {
                        g.incrementThemeIndex();
                        if (g.isLastTheme()) {
                            g.setFinished(true);
                            questionEnd(g, question);
                        } else {
                            themeEnd(g, theme);
                        }
                    } else {
                        questionEnd(g, question);
                    }
                }
            }
        }
    }

    private void prepareForAnswer(GameDTO g, GameDTO.Theme.Question question) {
        g.setPaused(false);
        g.setAnsweredUsers(new ArrayList<>());
        g.setQuestionInfo(new GameDTO.QuestionInfo(question.getAnswer(), question.getCost()));
    }

    private void sentQuestionChunk(GameDTO g, GameDTO.Theme.Question question, String message) {
        messagingTemplate.convertAndSend("/game/" + g.getId() + "/question", message);
        question.incrementLastIndex();
    }

    private void endGame(GameDTO g) {
        messagingTemplate.convertAndSend("/game/" + g.getId() + "/end", LocalDateTime.now());
        activeGames.remove(g.getId());
    }

    private void questionEnd(GameDTO g, GameDTO.Theme.Question question) {
        g.setPausedUntil(LocalDateTime.now().plusSeconds(5));
        messagingTemplate.convertAndSend("/game/" + g.getId() + "/question/end", question.getCost());
    }

    private void themeEnd(GameDTO g, GameDTO.Theme theme) {
        messagingTemplate.convertAndSend("/game/" + g.getId() + "/theme/end", theme.getName());
        g.setPausedUntil(LocalDateTime.now().plusSeconds(10));
    }

    private void questionStart(GameDTO g, GameDTO.Theme.Question question) {
        messagingTemplate.convertAndSend("/game/" + g.getId() + "/question/start", question.getCost());
        question.setCostSent(true);
        g.setPausedUntil(LocalDateTime.now().plusSeconds(3));
    }

    private void startTheme(GameDTO g, GameDTO.Theme theme) {
        messagingTemplate.convertAndSend("/game/" + g.getId() + "/theme/start", theme.getName());
        theme.setNameSent(true);
        g.setPausedUntil(LocalDateTime.now().plusSeconds(5));
    }
}
