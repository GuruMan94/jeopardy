package ge.tsotne.jeopardy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ge.tsotne.jeopardy.configuration.UserPrincipal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Audited
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "PLAYERS")
@Data
@NoArgsConstructor
@SequenceGenerator(name = "seqPlayers", sequenceName = "SEQ_PLAYERS", allocationSize = 1)
public class Player extends AuditedEntity {
    public enum Role {
        PLAYER, SHOWMAN, SPECTATOR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqPlayers")
    private Long id;

    @JsonIgnore
    @NotNull
    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID", insertable = false, updatable = false)
    private User user;

    @NotNull
    @Column(name = "USER_NAME", nullable = false)
    private String userName;

    @NotNull
    @Column(name = "POINT", nullable = false)
    private Integer point;

    @JsonIgnore
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
        super.prePersist();
        this.point = 0;
    }

    public Player(long gameId, Role role, UserPrincipal user) {
        this.gameId = gameId;
        this.role = role;
        this.userId = user.getId();
        this.userName = user.getUsername();
    }
}
