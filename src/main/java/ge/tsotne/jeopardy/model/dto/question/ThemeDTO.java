package ge.tsotne.jeopardy.model.dto.question;

import ge.tsotne.jeopardy.model.validation.ValidationWithId;
import ge.tsotne.jeopardy.model.validation.ValidationWithoutId;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class ThemeDTO {
    @NotNull(groups = {ValidationWithId.class})
    private Long id;

    @NotNull(groups = {ValidationWithoutId.class, ValidationWithId.class})
    private String name;

    private String description;

    @Valid
    @Size(min = 1, groups = {ValidationWithoutId.class, ValidationWithId.class})
    @NotNull(groups = {ValidationWithoutId.class, ValidationWithId.class})
    private List<QuestionDTO> questions;
}
