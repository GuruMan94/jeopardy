package ge.tsotne.jeopardy.model.dto.game.scheduler;

import ge.tsotne.jeopardy.model.Game;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GameDTO {
    private Long id;
    private Boolean paused = false;
    private LocalDateTime pausedUntil = LocalDateTime.now();
    private List<Theme> themes = new ArrayList<>();
    private int themeCount = 0;
    private int lastThemeIndex = 0;

    public void incrementThemeIndex() {
        lastThemeIndex++;
    }

    @Data
    public static class Theme {
        private String name;
        private Integer priority;
        private List<Question> questions = new ArrayList<>();
        private int questionCount = 0;
        private int lastQuestionIndex = 0;
        private Boolean nameSent = false;

        public void incrementQuestionIndex() {
            lastQuestionIndex++;
        }

        @Data
        public static class Question {
            private String[] text;
            private Integer priority;
            private Integer cost;
            private Boolean processed = false;
            private int lastIndex = 0;
            private Boolean costSent = false;

            public Question(ge.tsotne.jeopardy.model.Question question) {
                this.text = question.getQuestionText().split(" ");
                this.priority = question.getPriority();
                this.cost = question.getCost();
            }

            public void incrementLastIndex() {
                lastIndex++;
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

    public GameDTO(Game game) {
        this.id = game.getId();
        if (game.getQuestionPack() != null && !StringUtils.isEmpty(game.getQuestionPack().getThemes())) {
            this.themes = game.getQuestionPack().getThemes()
                    .stream()
                    .map(Theme::new)
                    .collect(Collectors.toList());
            this.themeCount = themes.size();
        }
    }
}
