package ge.tsotne.jeopardy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ge.tsotne.jeopardy.model.dto.game.GameDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.envers.Audited;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Min(value = 2)
    @NotNull
    @Column(name = "MAX_PLAYER_COUNT", nullable = false, updatable = false)
    private Integer maxPlayerCount;

    @Min(value = 1)
    @NotNull
    @Column(name = "SECONDS_FOR_ANSWER", nullable = false, updatable = false)
    private Integer secondsForAnswer;

    @NotNull
    @Column(name = "QUESTION_PACK_ID", nullable = false, updatable = false)
    private Long questionPackId;

    @JsonIgnore
    @BatchSize(size = 100)
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "QUESTION_PACK_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private QuestionPack questionPack;

    @NotNull
    @Enumerated
    @Column(name = "STATUS", nullable = false)
    private Status status;

    @Column(name = "START_DATE")
    LocalDateTime startDate;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH, mappedBy = "game")
    private List<Player> players = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        super.prePersist();
        this.status = Status.NEW;
    }

    public Game(GameDTO dto) {
        this.name = dto.getName();
        this.password = dto.getPassword();
        this.privateGame = !StringUtils.isEmpty(password);
        this.maxPlayerCount = dto.getMaxPlayerCount();
        this.secondsForAnswer = dto.getSecondsForAnswer();
        this.questionPackId = dto.getQuestionPackId();
    }

    public void start() {
        this.status = Game.Status.STARTED;
        this.startDate = LocalDateTime.now();
    }
}
