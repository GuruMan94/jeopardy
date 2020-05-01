package ge.tsotne.jeopardy.model.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class QuestionPackDTO {
    private Long id;
    @NotNull
    private String name;
    private String description;
    private String author;
    @Min(value = 1)
    @NotNull
    private Integer themeCount;
}
