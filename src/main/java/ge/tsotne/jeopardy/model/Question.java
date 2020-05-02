package ge.tsotne.jeopardy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ge.tsotne.jeopardy.model.dto.QuestionDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@BatchSize(size = 100)
@Audited
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "QUESTIONS")
@Data
@NoArgsConstructor
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
    private Integer cost;

    @NotNull
    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    @Column(name = "COMMENT")
    private String comment;

    @NotAudited
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "THEME_ID", referencedColumnName = "ID", nullable = false)
    private Theme theme;

    public Question(QuestionDTO dto, int priority) {
        this.questionText = dto.getQuestionText();
        this.answer = dto.getAnswer();
        this.cost = dto.getCost();
        this.priority = priority;
        this.comment = dto.getComment();
    }
}
