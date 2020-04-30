package ge.tsotne.jeopardy.controller;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HelloController {
    private SimpMessagingTemplate simpMessagingTemplate;
    private Map<Long, List<Long>> games = new HashMap<>();

    public HelloController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @GetMapping("/")
    public String hello() {
        return "/index";
    }

    @MessageMapping("/questions")
    public String processQuestion(@Payload String question, Principal principal) {
        System.out.println(principal.getName());
        return question.toUpperCase();
    }

    @MessageMapping("/personal/questions")
    public void processPersonalQuestion(@Payload String userName, Principal principal) {
        System.out.println(principal.getName());
        simpMessagingTemplate.convertAndSendToUser(userName, "/topic/personal/questions", LocalDate.now());
    }

    @MessageExceptionHandler
    @SendToUser("/error")
    public String handleException(Exception ex) {
        return "Got error: " + ex.getMessage();
    }

    @MessageMapping("/game.addUser")
    public void addUser(){
        throw new RuntimeException("TSOTNES_EXCEPTION");
    }
}
