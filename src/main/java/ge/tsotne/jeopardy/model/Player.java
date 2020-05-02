package ge.tsotne.jeopardy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Audited
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "PLAYERS")
@Data
@SequenceGenerator(name = "seqPlayers", sequenceName = "SEQ_PLAYERS", allocationSize = 1)
public class Player extends AuditedEntity {
    public enum Role {
        PLAYER, SHOWMAN, SPECTATOR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPlayers")
    private Long id;

    @NotNull
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "POINT", nullable = false)
    private Integer point;

    @NotNull
    @Column(name = "GAME_ID", nullable = false)
    private Long gameId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "GAME_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private Game game;

    @NotNull
    @Enumerated
    @Column(name = "ROLE", nullable = false)
    private Role role;

    @PrePersist
    public void prePersist() {
        this.point = 0;
    }

}
