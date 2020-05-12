package ge.tsotne.jeopardy.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserDTO {
    @NotNull
    private String userName;
    @NotNull
    private String password;
    private String email;
    private String firstName;
    private String lastName;

}
