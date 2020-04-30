package ge.tsotne.jeopardy.repository;

import ge.tsotne.jeopardy.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbInit implements CommandLineRunner {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
//        User user1 = new User("admin", passwordEncoder.encode("admin"),"admin@gmail.com");
//        User user2 = new User("tsotne", passwordEncoder.encode("tsotne"),"tsotne@gmail.com");
//        userRepository.saveAll(List.of(user1, user2));
    }
}
