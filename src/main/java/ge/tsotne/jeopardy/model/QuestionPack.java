package ge.tsotne.jeopardy.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "QUESTION_PACK")
@Data
@SequenceGenerator(name = "seqQuestionPacks", sequenceName = "SEQ_QUESTION_PACKS", allocationSize = 1)
public class QuestionPack extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqQuestionPacks")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "AUTHOR")
    private String author;

    @Min(value = 1)
    @NotNull
    @Column(name = "THEME_COUNT", nullable = false)
    private Integer themeCount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pack")
    private List<Theme> themes;
}
