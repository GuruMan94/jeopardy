package ge.tsotne.jeopardy.model;

import ge.tsotne.jeopardy.model.dto.QuestionPackDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OrderBy("priority")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pack")
    private List<Theme> themes = new ArrayList<>();

    public QuestionPack(QuestionPackDTO dto) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.author = dto.getAuthor();
        for (int i = 0; i < dto.getThemes().size(); i++) {
            themes.add(new Theme(dto.getThemes().get(i), i + 1));
        }
    }
}
