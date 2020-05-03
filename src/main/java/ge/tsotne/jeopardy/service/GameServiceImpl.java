package ge.tsotne.jeopardy.service;

import ge.tsotne.jeopardy.Utils;
import ge.tsotne.jeopardy.configuration.UserPrincipal;
import ge.tsotne.jeopardy.model.Game;
import ge.tsotne.jeopardy.model.Game_;
import ge.tsotne.jeopardy.model.Player;
import ge.tsotne.jeopardy.model.dto.game.EnterGameDTO;
import ge.tsotne.jeopardy.model.dto.game.GameDTO;
import ge.tsotne.jeopardy.model.dto.game.GameSearchDTO;
import ge.tsotne.jeopardy.repository.GameRepository;
import ge.tsotne.jeopardy.repository.PlayerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotNull;
import java.util.List;

import static ge.tsotne.jeopardy.model.Player.Role.SHOWMAN;

@Service
public class GameServiceImpl implements GameService {
    private GameRepository gameRepository;
    private QuestionPackService questionPackService;
    private PlayerRepository playerRepository;

    public GameServiceImpl(GameRepository gameRepository,
                           QuestionPackService questionPackService,
                           PlayerRepository playerRepository) {
        this.gameRepository = gameRepository;
        this.questionPackService = questionPackService;
        this.playerRepository = playerRepository;
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
    public Game create(GameDTO dto) {
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
    public void enter(long id, EnterGameDTO dto) {
        Game game = get(id);
        validate(game, dto);
        savePlayer(id, dto.getRole());
    }

    @Override
    public void start(long id) {
        if (isNotShowMan(id)) {
            throw new RuntimeException("ONLY_SHOWMAN_CANT_START_THE_GAME");
        }
        Game game = get(id);
        if (game.getStatus() != Game.Status.NEW) {
            throw new RuntimeException("CANT_START_GAME");
        }
        game.start();
        gameRepository.save(game);
    }

    @Override
    public void end(long id) {
        if (isNotShowMan(id)) {
            throw new RuntimeException("ONLY_SHOWMAN_CANT_END_THE_GAME");
        }
        Game game = get(id);
        game.end();
        gameRepository.save(game);
    }

    private boolean isNotShowMan(long gameId) {
        long userId = Utils.getCurrentUserIdNotNull();
        return playerRepository.countByGameIdAndUserIdAndRoleAndActiveTrue(gameId, userId, SHOWMAN) == 0;
    }

    private boolean isMember(long gameId) {
        long userId = Utils.getCurrentUserIdNotNull();
        return playerRepository.countByGameIdAndUserIdAndActiveTrue(gameId, userId) > 0;
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
            //TODO რაღაც რაოდენობის ცდის შემდეგ დაებლოკოს 5 წუთით მაგალითად
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
}
