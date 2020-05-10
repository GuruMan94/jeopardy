package ge.tsotne.jeopardy.controller;

import ge.tsotne.jeopardy.Utils;
import ge.tsotne.jeopardy.model.Game;
import ge.tsotne.jeopardy.model.dto.game.CheckAnswerDTO;
import ge.tsotne.jeopardy.model.dto.game.EnterGameDTO;
import ge.tsotne.jeopardy.model.dto.game.GameDTO;
import ge.tsotne.jeopardy.model.dto.game.GameSearchDTO;
import ge.tsotne.jeopardy.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
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
    @GetMapping("/game/{id}")
    public Game get(@PathVariable Long id) {
        return gameService.get(id);
    }

    @ResponseBody
    @GetMapping("/game/active")
    public Game getActive() {
        return gameService.getActive();
    }

    @ResponseBody
    @PostMapping("/game/{id}/enter")
    public void enter(@PathVariable Long id, @RequestBody @Valid EnterGameDTO dto) {
        gameService.enter(id, dto);
    }

    @ResponseBody
    @PostMapping("/game/{id}/start")
    public void start(@PathVariable Long id) {
        gameService.start(id);
    }

    @ResponseBody
    @PostMapping("/game/{id}/answer")
    public void answer(@PathVariable Long id) {
        gameService.answer(id);
    }

    @ResponseBody
    @PostMapping("/game/{id}/answer/check")
    public void checkAnswer(@PathVariable Long id, @RequestBody CheckAnswerDTO dto) {
        gameService.checkAnswer(id, dto.getCorrect());
    }

    @ResponseBody
    @PostMapping("/game/{id}/pause")
    public void pause(@PathVariable Long id) {
        gameService.pause(id);
    }

    @ResponseBody
    @PostMapping("/game/{id}/resume")
    public void resume(@PathVariable Long id) {
        gameService.resume(id);
    }

    @ResponseBody
    @PostMapping("/game/{id}/end")
    public void end(@PathVariable Long id) {
        gameService.end(id);
    }

    @ResponseBody
    @GetMapping("/game/{id}/points")
    public List<ge.tsotne.jeopardy.model.dto.game.scheduler.GameDTO.Player> getPoints(@PathVariable Long id) {
        return gameService.getPoints(id);
    }

    @ResponseBody
    @GetMapping("/game/{id}/player")
    public ge.tsotne.jeopardy.model.dto.game.scheduler.GameDTO.Player getPlayer(@PathVariable Long id) {
        return gameService.getPlayerByUserId(id, Utils.getCurrentUserIdNotNull());
    }

    //TODO get game results players with points,duration,percentage % of correct answers,answers count

    //TODO game history თამაშის ისტორია ვინ რამდენჯერ უპასუხა

    //TODO game history ყველა თამაშის ისტორიული რეპორტი, სულ ნაპასუხები კითხვები და კოეფიციენტები
}
