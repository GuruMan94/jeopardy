package ge.tsotne.jeopardy.repository;

import ge.tsotne.jeopardy.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

    int countByNameAndStatusIn(String name, List<Game.Status> statuses);
}
