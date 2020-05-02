package ge.tsotne.jeopardy.controller;

import ge.tsotne.jeopardy.service.GameService;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {
    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }
}
