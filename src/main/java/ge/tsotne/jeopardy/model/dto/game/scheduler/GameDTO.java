package ge.tsotne.jeopardy.model.dto.game.scheduler;

import ge.tsotne.jeopardy.model.Game;
import lombok.*;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// TODO შედულერია დასაწერი რომელიც დაითვლის პასუხისთვის დროს და ასევე თუ არავინ უპასუხა მაგ დროსაც
@Data
public class GameDTO {
    private long id;
    @Getter(AccessLevel.NONE)
    private boolean canAnswer = false;
    private boolean isFinished = false;
    private long savedPausedSeconds = 0;
    private int lastThemeIndex = 0;
    private long showManUserId;
    private QuestionInfo questionInfo;
    private List<Theme> themes = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private LocalDateTime pausedUntil = LocalDateTime.now();

    public void incrementThemeIndex() {
        lastThemeIndex++;
    }

    public boolean isLastTheme() {
        return this.getThemes().size() == this.getLastThemeIndex();
    }

    public boolean canAnswer(long userId) {
        return canAnswer && !hasAnswered(userId);
    }

    public Player getAnsweringPlayer() {
        return players.stream()
                .filter(u -> u.getAnswerState() == Player.AnswerState.ANSWERING)
                .findFirst()
                .orElse(null);
    }

    private boolean hasAnswered(long userId) {
        return players.stream()
                .anyMatch(u -> u.userId == userId
                        && u.getAnswerState() == Player.AnswerState.ALREADY_ANSWERED);
    }

    public void clearAnswers() {
        players.forEach(p -> p.setAnswerState(Player.AnswerState.NONE));
    }

    public Theme getCurrentTheme() {
        if (lastThemeIndex >= this.getThemes().size()) return null;
        return this.getThemes().get(this.getLastThemeIndex());
    }

    public boolean isPaused() {
        return LocalDateTime.now().compareTo(this.pausedUntil) < 0;
    }

    @Data
    public static class Theme {
        private String name;
        private int priority;
        private List<Question> questions = new ArrayList<>();
        private int lastQuestionIndex = 0;
        private boolean nameSent = false;

        public void incrementQuestionIndex() {
            lastQuestionIndex++;
        }

        public boolean isLastQuestion() {
            return this.getQuestions().size() == this.getLastQuestionIndex();
        }

        public Question getCurrentQuestion() {
            if (lastQuestionIndex >= this.getQuestions().size()) return null;
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

            public boolean isFirstChunk() {
                return 0 == this.getLastIndex();
            }

            public String getCurrentChunk() {
                if (this.lastIndex >= this.getText().length) return null;
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
        }
    }

    @Data
    public static class Player {
        public enum AnswerState {
            NONE, ANSWERING, ALREADY_ANSWERED
        }

        private long userId;
        private int point = 0;
        private AnswerState answerState = AnswerState.NONE;
        private ge.tsotne.jeopardy.model.Player.Role role;
        private LocalDateTime startedAnswering;

        public Player(ge.tsotne.jeopardy.model.Player player) {
            this.userId = player.getUserId();
            this.role = player.getRole();
        }

        public void addPoint(int cost) {
            this.point += cost;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionInfo {
        private String answer;
        private int cost;
    }

    public GameDTO(Game game) {
        this.id = game.getId();
        if (game.getQuestionPack() != null && !StringUtils.isEmpty(game.getQuestionPack().getThemes())) {
            this.themes = game.getQuestionPack().getThemes()
                    .stream()
                    .map(Theme::new)
                    .collect(Collectors.toList());
        }
        this.players = game.getPlayers()
                .stream()
                .filter(p -> p.getRole() == ge.tsotne.jeopardy.model.Player.Role.PLAYER
                        || p.getRole() == ge.tsotne.jeopardy.model.Player.Role.SHOWMAN)
                .map(Player::new).collect(Collectors.toList());
        this.setPausedUntil(LocalDateTime.now().plusSeconds(2));
    }
}
