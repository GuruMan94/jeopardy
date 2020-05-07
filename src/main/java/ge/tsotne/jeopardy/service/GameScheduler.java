package ge.tsotne.jeopardy.service;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class GameScheduler {
    private final GameService gameService;

    public GameScheduler(GameService gameService) {
        this.gameService = gameService;
    }

    @Scheduled(cron = "* * * * * *")
    public void prepareQuestions() {
        gameService.getActiveGames()
                .values()
                .forEach(gameService::sendQuestionChunk);
    }

    // გვაქვს 2 შედულერი რომელიც ეშვება ყოველ წამს.

    // პირველი შედულერი დაასელექტებს აქტიური თამაშებიდან პირველივე დაუმუშავებელ(პრიორიტეტით დალაგებულია) თემას და კითხვებს
    // თამაშის იდ - თემის სახელი,კითხვა, კითხვის პრიორიტეტი, ქულა


    // ამოასელექტებ ერთ თემას კითხვებით და ჩააგდოს hashmap-ში. თუ მეპში არის ამ თამაშის თემა ჯერ არ ამოასელექტო
    // პირველ რიგში უნდა გაიგზავნოს სათაური და შეტყობინება ახალი თემის დაწყების შესახებ
    // გარკვეული პაუზის შემდეგ მაგალითად 5 წამი უნდა დაიწყოს კითხვების გაგზავნა
    // კითხვის გაგზავნამდე ჯერ უნდა გაიგზავნოს ქულა და გარკვეული პაუზის შემდეგ თვითონ კითხვა


}
