package shaswata.taskmanager;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;




@RestController
@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class TaskManagerApplication {

	@RequestMapping("/foo")
	public String home() {
		return "Hello Docker World";
	}

	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}


}
