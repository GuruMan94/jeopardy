package ge.tsotne.jeopardy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "THEMES")
@Data
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
    @Column(name = "QUESTION_COUNT", nullable = false)
    private Integer questionCount;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "theme")
    private List<Question> questions;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PACK_ID", referencedColumnName = "ID", nullable = false)
    private QuestionPack pack;
}
