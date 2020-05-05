package ge.tsotne.jeopardy.service;

import ge.tsotne.jeopardy.model.Game;
import ge.tsotne.jeopardy.model.dto.game.EnterGameDTO;
import ge.tsotne.jeopardy.model.dto.game.GameDTO;
import ge.tsotne.jeopardy.model.dto.game.GameSearchDTO;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface GameService {
    List<Game> search(GameSearchDTO dto);

    Game create(GameDTO dto);

    void enter(long id, EnterGameDTO dto);

    Game get(long id);

    void start(long id);

    void end(long id);

    Game getActive();

    void sendQuestionChunk(ge.tsotne.jeopardy.model.dto.game.scheduler.GameDTO dto);

    ConcurrentHashMap<Long, ge.tsotne.jeopardy.model.dto.game.scheduler.GameDTO> getActiveGames();

    void pause(long id);

    void resume(long id);

    void answer(long id);
}
