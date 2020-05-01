package ge.tsotne.jeopardy.service;

import ge.tsotne.jeopardy.model.QuestionPack;
import ge.tsotne.jeopardy.model.dto.QuestionSearchParams;
import org.springframework.data.domain.Page;

import javax.validation.constraints.NotNull;

public interface QuestionPackService {
    Page<QuestionPack> find(QuestionSearchParams searchParams);

    QuestionPack get(long id);

    QuestionPack add(@NotNull QuestionPack pack);

    QuestionPack update(long id, @NotNull QuestionPack pack);

    void delete(Long id);
}
