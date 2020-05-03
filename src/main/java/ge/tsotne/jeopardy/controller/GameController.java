package ge.tsotne.jeopardy.controller;

import ge.tsotne.jeopardy.model.Game;
import ge.tsotne.jeopardy.model.dto.game.EnterGameDTO;
import ge.tsotne.jeopardy.model.dto.game.GameDTO;
import ge.tsotne.jeopardy.model.dto.game.GameSearchDTO;
import ge.tsotne.jeopardy.service.GameService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
public class GameController {
    private GameService gameService;
    private SimpMessagingTemplate messagingTemplate;

    public GameController(GameService gameService,
                          SimpMessagingTemplate messagingTemplate) {
        this.gameService = gameService;
        this.messagingTemplate = messagingTemplate;
    }

    @ResponseBody
    @PostMapping("/games")
    public List<Game> search(@RequestBody GameSearchDTO dto) {
        return gameService.search(dto);
    }

    @ResponseBody
    @PostMapping("/game")
    public Game create(@RequestBody @Valid GameDTO dto) {
        return gameService.create(dto);
    }

    @ResponseBody
    @PostMapping("/game/{id}/enter")
    public void enter(@PathVariable Long id, @RequestBody @Valid EnterGameDTO dto) {
        gameService.enter(id, dto);
        messagingTemplate.convertAndSend("/game/" + id + "/enter", LocalDate.now());
    }

    @ResponseBody
    @PostMapping("/game/{id}/start")
    public void start(@PathVariable Long id) {
        gameService.start(id);
        messagingTemplate.convertAndSend("/game/" + id + "/start", LocalDate.now());
    }


    //TODO თამაშის დაწყება,დაპაუზება

    //TODO game history
}
