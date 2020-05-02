package ge.tsotne.jeopardy.service;

import ge.tsotne.jeopardy.model.Game;
import ge.tsotne.jeopardy.model.dto.game.GameDTO;
import ge.tsotne.jeopardy.model.dto.game.GameSearchDTO;

import java.util.List;

public interface GameService {
    List<Game> search(GameSearchDTO dto);

    Game create(GameDTO dto);
}
