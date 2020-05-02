package ge.tsotne.jeopardy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ge.tsotne.jeopardy.model.dto.ThemeDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@BatchSize(size = 100)
@Audited
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "THEMES")
@Data
@NoArgsConstructor
@SequenceGenerator(name = "seqThemes", sequenceName = "SEQ_THEMES", allocationSize = 1)
public class Theme extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqThemes")
    private Long id;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Column(name = "PRIORITY", nullable = false)
    private Integer priority;

    @OrderBy("priority")
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "theme")
    private List<Question> questions = new ArrayList<>();

    @NotAudited
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PACK_ID", referencedColumnName = "ID", nullable = false)
    private QuestionPack pack;

    public Theme(ThemeDTO dto, int priority) {
        this.name = dto.getName();
        this.description = dto.getDescription();
        this.priority = priority;
        for (int i = 0; i < dto.getQuestions().size(); i++) {
            questions.add(new Question(dto.getQuestions().get(i), i + 1));
        }
    }
}
