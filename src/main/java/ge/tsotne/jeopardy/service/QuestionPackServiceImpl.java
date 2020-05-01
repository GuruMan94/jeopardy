package ge.tsotne.jeopardy.service;

import ge.tsotne.jeopardy.Utils;
import ge.tsotne.jeopardy.model.QuestionPack;
import ge.tsotne.jeopardy.model.Theme;
import ge.tsotne.jeopardy.model.dto.QuestionPackDTO;
import ge.tsotne.jeopardy.model.dto.QuestionSearchParams;
import ge.tsotne.jeopardy.repository.QuestionPackRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
public class QuestionPackServiceImpl implements QuestionPackService {
    private QuestionPackRepository questionPackRepository;

    public QuestionPackServiceImpl(QuestionPackRepository questionPackRepository) {
        this.questionPackRepository = questionPackRepository;
    }

    @Override
    public Page<QuestionPack> find(QuestionSearchParams searchParams) {
        long userId = Utils.getCurrentUserIdNotNull();
        return questionPackRepository.findByCreatedBy(userId, PageRequest.of(0, Integer.MAX_VALUE));
    }

    @Override
    public QuestionPack get(long id) {
        long userId = Utils.getCurrentUserIdNotNull();
        return questionPackRepository.findByIdAndCreatedByAndActiveTrue(id, userId)
                .orElseThrow(() -> new RuntimeException("PACKET_NOT_FOUND"));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public QuestionPack add(@NotNull QuestionPackDTO dto) {
        QuestionPack pack = new QuestionPack(dto);
        prepareBeforeSave(pack, false);
        return questionPackRepository.save(pack);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public QuestionPack update(long id, @NotNull QuestionPackDTO dto) {
//        validatePackBeforeSave(pack, false);
//        return questionPackRepository.save(pack);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Long id) {
    }

    private boolean isOwner(Long userId) {
        return userId.equals(Utils.getCurrentUserIdNotNull());
    }

    private void prepareBeforeSave(@NotNull QuestionPack pack, boolean isUpdate) {
        pack.getThemes().forEach(t -> {
            t.setPack(pack);
            t.getQuestions().forEach(q -> q.setTheme(t));
        });
        validatePackBeforeSave(pack, isUpdate);
    }

    private void validatePackBeforeSave(@NotNull QuestionPack pack, boolean isUpdate) {
        if (isUpdate) {
            if (!isOwner(pack.getCreatedBy())) {
                throw new RuntimeException("NOT_ALLOWED");
            }
        }
        if (pack.getThemeCount() != pack.getThemes().size()) {
            throw new RuntimeException("INCORRECT_THEME_COUNT");
        }
        pack.getThemes().forEach(this::validateThemeBeforeSave);
    }

    private void validateThemeBeforeSave(@NotNull Theme t) {
        if (!t.getQuestionCount().equals(t.getQuestions().size())) {
            throw new RuntimeException("INCORRECT_QUESTION_COUNT");
        }
    }
}
