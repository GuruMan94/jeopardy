package ge.tsotne.jeopardy.service;

import ge.tsotne.jeopardy.model.User;
import ge.tsotne.jeopardy.model.dto.UserDTO;
import ge.tsotne.jeopardy.model.projection.UserNameOnly;
import ge.tsotne.jeopardy.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String getUserNameById(long id) {
        UserNameOnly user = userRepository.getUserNameById(id)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));
        return user.getUserName();
    }

    public boolean exists(@NotNull String userName) {
        return userRepository.countByUserName(userName) > 0;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public User register(UserDTO dto) {
        if (exists(dto.getUserName())) {
            throw new RuntimeException("USER_ALREADY_EXISTS");
        }
        User user = new User(dto, passwordEncoder);
        return userRepository.save(user);
    }
}
