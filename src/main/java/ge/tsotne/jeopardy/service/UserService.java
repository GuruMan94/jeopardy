package ge.tsotne.jeopardy.service;


import ge.tsotne.jeopardy.model.User;
import ge.tsotne.jeopardy.model.dto.UserDTO;

public interface UserService {
    String getUserNameById(long id);

    User register(UserDTO dto);

    void login(UserDTO dto);

    boolean exists(String userName);
}
