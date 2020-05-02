package ge.tsotne.jeopardy.service;

import ge.tsotne.jeopardy.repository.GameRepository;
import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {
    private GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }
}
