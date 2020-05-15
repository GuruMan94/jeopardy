package ge.tsotne.jeopardy.model;

import ge.tsotne.jeopardy.model.dto.UserDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Audited
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "USERS")
@SequenceGenerator(name = "SEQ_USER", sequenceName = "SEQ_USER", allocationSize = 1)
@Data
@NoArgsConstructor
public class User extends AuditedEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER")
    private Long id;

    @NotNull
    @Column(name = "USER_NAME", unique = true, nullable = false)
    private String userName;

    @NotNull
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    public User(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public User(UserDTO dto, PasswordEncoder passwordEncoder) {
        this.userName = dto.getUserName();
        this.password = passwordEncoder.encode(dto.getPassword());
        this.email = dto.getEmail();
        this.firstName = dto.getFirstName();
        this.lastName = dto.getLastName();
    }
}
