package ge.tsotne.jeopardy.repository;

import ge.tsotne.jeopardy.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

    int countByNameAndStatusIn(String name, List<Game.Status> statuses);

    @Query("SELECT g FROM Game g INNER JOIN g.players p WHERE p.userId=:playerUserId AND p.active=true")
    Optional<Game> findByPlayerId(long playerUserId);
}
