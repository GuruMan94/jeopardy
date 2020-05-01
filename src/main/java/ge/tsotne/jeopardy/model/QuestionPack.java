package ge.tsotne.jeopardy.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
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
    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "DESCRIPTION")
    private String description;
    @Column(name = "AUTHOR")
    private String author;
    @Column(name = "THEME_COUNT", nullable = false)
    private Integer themeCount;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pack")
    private List<Theme> themes;
}
