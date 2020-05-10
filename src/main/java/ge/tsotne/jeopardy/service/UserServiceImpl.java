package ge.tsotne.jeopardy.service;

import ge.tsotne.jeopardy.model.projection.UserNameOnly;
import ge.tsotne.jeopardy.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getUserNameById(long id) {
        UserNameOnly user = userRepository.getUserNameById(id)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));
        return user.getUserName();
    }
}
