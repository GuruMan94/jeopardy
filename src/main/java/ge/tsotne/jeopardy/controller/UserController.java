package ge.tsotne.jeopardy.controller;

import ge.tsotne.jeopardy.model.User;
import ge.tsotne.jeopardy.model.dto.UserDTO;
import ge.tsotne.jeopardy.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserDTO dto) {
        return ResponseEntity.ok(userService.register(dto));
    }

    @PostMapping("/user/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserDTO dto) {
        userService.login(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/check")
    public ResponseEntity<Boolean> exists(@RequestParam String userName) {
        return ResponseEntity.ok(userService.exists(userName));
    }
}
