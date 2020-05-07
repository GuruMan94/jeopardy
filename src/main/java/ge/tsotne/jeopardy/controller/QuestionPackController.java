package ge.tsotne.jeopardy.controller;

import ge.tsotne.jeopardy.model.QuestionPack;
import ge.tsotne.jeopardy.model.dto.question.QuestionPackDTO;
import ge.tsotne.jeopardy.model.dto.question.QuestionSearchParams;
import ge.tsotne.jeopardy.model.validation.ValidationWithId;
import ge.tsotne.jeopardy.model.validation.ValidationWithoutId;
import ge.tsotne.jeopardy.service.QuestionPackService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
public class QuestionPackController {
    private final QuestionPackService questionPackService;

    public QuestionPackController(QuestionPackService questionPackService) {
        this.questionPackService = questionPackService;
    }

    @ResponseBody
    @PostMapping("/questions")
    public Page<QuestionPack> find(@RequestBody QuestionSearchParams searchParams) {
        return questionPackService.find(searchParams);
    }

    @ResponseBody
    @GetMapping("/question/{id}")
    public QuestionPack get(@PathVariable("id") Long id) {
        return questionPackService.get(id);
    }

    @ResponseBody
    @PostMapping("/question")
    public QuestionPack add(@Validated(ValidationWithoutId.class) @RequestBody QuestionPackDTO dto) {
        return questionPackService.add(dto);
    }

    @ResponseBody
    @PutMapping("/question/{id}")
    public QuestionPack update(@PathVariable("id") Long id, @Validated(ValidationWithId.class) @RequestBody QuestionPackDTO dto) {
        return questionPackService.update(id, dto);
    }

    @ResponseBody
    @DeleteMapping("/question/{id}")
    public void delete(@PathVariable("id") Long id) {
        questionPackService.delete(id);
    }
}
