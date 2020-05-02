package ge.tsotne.jeopardy.model.dto.game;

import ge.tsotne.jeopardy.model.Player;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class GameDTO {
    @NotNull
    private String name;
    private String password;
    @Min(value = 2)
    @NotNull
    private Integer maxPlayerCount;
    @Min(value = 1)
    @NotNull
    private Integer secondsForAnswer;
    @NotNull
    private Long questionPackId;
    @NotNull
    private Player.Role role;
}
