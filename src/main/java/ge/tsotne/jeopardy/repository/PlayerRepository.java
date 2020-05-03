package ge.tsotne.jeopardy.repository;

import ge.tsotne.jeopardy.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    int countByGameIdAndUserIdAndRoleAndActiveTrue(long gameId, long userId, Player.Role role);

    int countByGameIdAndUserIdAndActiveTrue(long gameId, long userId);
}
