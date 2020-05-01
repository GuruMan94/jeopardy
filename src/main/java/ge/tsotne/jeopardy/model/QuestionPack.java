package ge.tsotne.jeopardy.model;

import ge.tsotne.jeopardy.model.dto.QuestionPackDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Audited
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "QUESTION_PACK")
@Data
@NoArgsConstructor
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

    @OrderBy("priority")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pack")
    private List<Theme> themes;

    public QuestionPack(QuestionPackDTO dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.author = dto.getAuthor();
        this.themeCount = dto.getThemeCount();
        this.themes = dto.getThemes()
                .stream()
                .map(Theme::new)
                .collect(Collectors.toList());
    }
}
