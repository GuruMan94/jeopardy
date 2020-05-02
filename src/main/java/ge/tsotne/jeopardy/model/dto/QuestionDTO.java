package ge.tsotne.jeopardy.model.dto;

import ge.tsotne.jeopardy.model.validation.ValidationWithId;
import ge.tsotne.jeopardy.model.validation.ValidationWithoutId;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class QuestionDTO {
    @NotNull(groups = {ValidationWithId.class})
    private Long id;

    @NotNull(groups = {ValidationWithoutId.class, ValidationWithId.class})
    private String questionText;

    @NotNull(groups = {ValidationWithoutId.class, ValidationWithId.class})
    private String answer;

    @Min(value = 1, groups = {ValidationWithoutId.class, ValidationWithId.class})
    @NotNull(groups = {ValidationWithoutId.class, ValidationWithId.class})
    private Integer cost;

    private String comment;
}
