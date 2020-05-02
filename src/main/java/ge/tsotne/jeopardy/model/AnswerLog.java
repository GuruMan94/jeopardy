package ge.tsotne.jeopardy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ANSWER_LOG")
@Data
@SequenceGenerator(name = "seqAnswerLog", sequenceName = "SEQ_ANSWER_LOG", allocationSize = 1)
public class AnswerLog extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqAnswerLog")
    private Long id;

    @NotNull
    @Column(name = "POINT", nullable = false)
    private Integer point;

    @NotNull
    @Column(name = "QUESTION_ID", nullable = false)
    private Long questionId;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Question question;
}
