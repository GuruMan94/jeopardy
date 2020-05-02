package ge.tsotne.jeopardy.model.dto.game;

import ge.tsotne.jeopardy.model.Player;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class EnterGameDTO {
    @NotNull
    private Player.Role role;
    private String password;
}
