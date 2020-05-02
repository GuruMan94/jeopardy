package ge.tsotne.jeopardy.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Audited
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "GAMES")
@Data
@NoArgsConstructor
@SequenceGenerator(name = "seqGames", sequenceName = "SEQ_GAMES", allocationSize = 1)
public class Game extends AuditedEntity {
    public enum Status {
        NEW, STARTED, FINISHED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGames")
    private Long id;

    @NotNull
    @Column(name = "NAME", nullable = false, updatable = false)
    private String name;

    @Column(name = "PASSWORD", updatable = false)
    private String password;

    @NotNull
    @Column(name = "PRIVATE", nullable = false, columnDefinition = "boolean default false", updatable = false)
    private Boolean privateGame;

    @NotNull
    @Column(name = "MAX_PLAYER_COUNT", nullable = false, updatable = false)
    private Integer maxPlayerCount;

    @NotNull
    @Column(name = "SECONDS_FOR_ANSWER", nullable = false, updatable = false)
    private Integer secondsForAnswer;

    @NotNull
    @Column(name = "QUESTION_PACK_ID", nullable = false, updatable = false)
    private Long questionPackId;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "QUESTION_PACK_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private QuestionPack questionPack;

    @NotNull
    @Enumerated
    @Column(name = "STATUS", nullable = false)
    private Status status;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, mappedBy = "game")
    private List<Player> players;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.status = Status.NEW;
    }

}
