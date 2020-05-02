package ge.tsotne.jeopardy.service;

import ge.tsotne.jeopardy.model.QuestionPack;
import ge.tsotne.jeopardy.model.dto.QuestionPackDTO;
import ge.tsotne.jeopardy.model.dto.QuestionSearchParams;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

public interface QuestionPackService {
    Page<QuestionPack> find(QuestionSearchParams searchParams);

    QuestionPack get(long id);

    boolean exists(long id);

    QuestionPack add(@NotNull QuestionPackDTO dto);

    QuestionPack update(long id, @NotNull QuestionPackDTO dto);

    void delete(Long id);
}
