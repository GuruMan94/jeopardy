package ge.tsotne.jeopardy.repository;

import ge.tsotne.jeopardy.model.QuestionPack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionPackRepository extends JpaRepository<QuestionPack, Long> {

    Optional<QuestionPack> findByIdAndCreatedByAndActiveTrue(long id, long createdBy);

    Page<QuestionPack> findByCreatedBy(long userId, Pageable pageable);
}
