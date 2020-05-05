package ge.tsotne.jeopardy.model.dto.game.scheduler;

import ge.tsotne.jeopardy.model.Game;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class GameDTO {
    private long id;
    private boolean paused = false;
    private LocalDateTime pausedUntil = LocalDateTime.now();
    private List<Theme> themes = new ArrayList<>();
    private int themeCount = 0;
    private int lastThemeIndex = 0;
    private String correctAnswer;
    private boolean canAnswer = false;
    private long showManUserId;
    private List<Player> players = new ArrayList<>();
    private List<Long> answeredUserIds = new ArrayList<>();

    public void incrementThemeIndex() {
        lastThemeIndex++;
    }

    public boolean isLastTheme() {
        return this.getThemeCount() == this.getLastThemeIndex();
    }

    public Theme getCurrentTheme() {
        return this.getThemes().get(this.getLastThemeIndex());
    }

    @Data
    public static class Theme {
        private String name;
        private int priority;
        private List<Question> questions = new ArrayList<>();
        private int questionCount = 0;
        private int lastQuestionIndex = 0;
        private boolean nameSent = false;

        public void incrementQuestionIndex() {
            lastQuestionIndex++;
        }

        public boolean isLastQuestion() {
            return this.getQuestionCount() == this.getLastQuestionIndex();
        }

        public Question getCurrentQuestion() {
            return this.getQuestions().get(this.getLastQuestionIndex());
        }

        @Data
        public static class Question {
            private String[] text;
            private String answer;
            private int priority;
            private int cost;
            private int lastIndex = 0;
            private boolean costSent = false;

            public Question(ge.tsotne.jeopardy.model.Question question) {
                this.text = question.getQuestionText().split(" ");
                this.answer = question.getAnswer();
                this.priority = question.getPriority();
                this.cost = question.getCost();
            }

            public void incrementLastIndex() {
                lastIndex++;
            }

            public boolean isLastChunk() {
                return this.getText().length == this.getLastIndex();
            }

            public String getCurrentChunk() {
                return this.getText()[this.getLastIndex()];
            }
        }

        public Theme(ge.tsotne.jeopardy.model.Theme theme) {
            this.name = theme.getName();
            this.priority = theme.getPriority();
            this.questions = theme.getQuestions()
                    .stream()
                    .map(Question::new)
                    .collect(Collectors.toList());
            this.questionCount = questions.size();
        }
    }

    @Data
    public static class Player {
        private long playerUserId;
        private int point = 0;
        private int placeId;

        public Player(ge.tsotne.jeopardy.model.Player player) {
            this.playerUserId = player.getUserId();
        }
    }

    public GameDTO(Game game) {
        this.id = game.getId();
        if (game.getQuestionPack() != null && !StringUtils.isEmpty(game.getQuestionPack().getThemes())) {
            this.themes = game.getQuestionPack().getThemes()
                    .stream()
                    .map(Theme::new)
                    .collect(Collectors.toList());
            this.themeCount = themes.size();
        }
        Optional<ge.tsotne.jeopardy.model.Player> showMan = game.getPlayers()
                .stream()
                .filter(p -> p.getRole() == ge.tsotne.jeopardy.model.Player.Role.SHOWMAN)
                .findFirst();
        showMan.ifPresent(player -> this.showManUserId = player.getUserId());
        this.players = game.getPlayers()
                .stream()
                .filter(p -> p.getRole() == ge.tsotne.jeopardy.model.Player.Role.PLAYER)
                .map(Player::new).collect(Collectors.toList());
    }
}