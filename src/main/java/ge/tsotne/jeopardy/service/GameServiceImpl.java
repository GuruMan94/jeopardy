package ge.tsotne.jeopardy.service;

import ge.tsotne.jeopardy.model.Game;
import ge.tsotne.jeopardy.model.Game_;
import ge.tsotne.jeopardy.model.dto.game.GameDTO;
import ge.tsotne.jeopardy.model.dto.game.GameSearchDTO;
import ge.tsotne.jeopardy.repository.GameRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {
    private GameRepository gameRepository;
    private QuestionPackService questionPackService;

    public GameServiceImpl(GameRepository gameRepository, QuestionPackService questionPackService) {
        this.gameRepository = gameRepository;
        this.questionPackService = questionPackService;
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
    public Game create(GameDTO dto) {
        if (exists(dto.getName())) {
            throw new RuntimeException("GAME_ALREADY_EXISTS_WITH_THIS_NAME");
        }
        if (!questionPackService.exists(dto.getQuestionPackId())) {
            throw new RuntimeException("QUESTION_PACK_ID_NOT_FOUND");
        }
        Game game = new Game(dto);
        return gameRepository.save(game);
    }

    public boolean exists(String name) {
        return gameRepository.countByNameAndStatusIn(name, List.of(Game.Status.NEW, Game.Status.STARTED)) > 0;
    }
}
