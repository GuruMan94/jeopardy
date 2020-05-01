package ge.tsotne.jeopardy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JeopardyApplication {

	public static void main(String[] args) {
		SpringApplication.run(JeopardyApplication.class, args);
	}

}
