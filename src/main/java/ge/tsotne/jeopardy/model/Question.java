package ge.tsotne.jeopardy.model;

import java.math.BigDecimal;

//@Entity
//@Table(name = "QUESTIONS")
//@Data
public class Question {
    private Long id;
    private String text;
    private String answer;
    private BigDecimal cost;
    private Long packId;
    private Integer priority;
}
