package ge.tsotne.jeopardy.repository;

import ge.tsotne.jeopardy.model.User;
import ge.tsotne.jeopardy.model.projection.UserNameOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(@NotNull String userName);

    int countByUserName(@NotNull String userName);

    @Query("SELECT u.userName FROM User u WHERE u.id=:id")
    Optional<UserNameOnly> getUserNameById(long id);
}
