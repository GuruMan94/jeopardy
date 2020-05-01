package ge.tsotne.jeopardy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "QUESTIONS")
@Data
@SequenceGenerator(name = "seqQuestions", sequenceName = "SEQ_QUESTIONS", allocationSize = 1)
public class Question extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqQuestions")
    private Long id;

    @NotNull
    @Column(name = "QUESTION_TEXT", nullable = false)
    private String questionText;

    @NotNull
    @Column(name = "ANSWER", nullable = false)
    private String answer;

    @Min(value = 1)
    @NotNull
    @Column(name = "COST", nullable = false)
    private BigDecimal cost;

    @NotNull
    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    @Column(name = "COMMENT", nullable = false)
    private String comment;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "THEME_ID", referencedColumnName = "ID", nullable = false)
    private Theme theme;
}
