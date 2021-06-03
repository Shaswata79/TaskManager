package shaswata.taskmanager;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shaswata.taskmanager.model.AdminAccount;
import shaswata.taskmanager.repository.AdminRepository;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@RestController
@SpringBootApplication
public class TaskManagerApplication {

	@Value("${rootAdmin.email}")
	private String rootEmail;

	@Value("${rootAdmin.password}")
	private String rootPassword;

	@Autowired
	AdminRepository adminRepo;

	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}



	@Bean
	public InitializingBean initializeSystem() {
		//initialize a root admin account if it does not already exist.
		return () -> {
			if (adminRepo.findAdminAccountByEmail(rootEmail) == null) {
				AdminAccount rootAdmin = new AdminAccount();
				rootAdmin.setEmail(rootEmail);
				rootAdmin.setPassword(rootPassword);
				rootAdmin.setName("root");
				adminRepo.save(rootAdmin);
			}
		};
	}



}
