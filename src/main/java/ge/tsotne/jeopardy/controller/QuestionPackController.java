package ge.tsotne.jeopardy.controller;

import ge.tsotne.jeopardy.model.QuestionPack;
import ge.tsotne.jeopardy.model.dto.QuestionSearchParams;
import ge.tsotne.jeopardy.service.QuestionPackService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
public class QuestionPackController {
    private QuestionPackService questionPackService;

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
    public QuestionPack add(@Valid @RequestBody QuestionPack pack) {
        return questionPackService.add(pack);
    }

    @ResponseBody
    @PutMapping("/question/{id}")
    public QuestionPack update(@PathVariable("id") Long id, @Valid @RequestBody QuestionPack pack) {
        return questionPackService.update(id, pack);
    }

    @ResponseBody
    @DeleteMapping("/question/{id}")
    public void delete(@PathVariable("id") Long id) {
        questionPackService.delete(id);
    }
}
