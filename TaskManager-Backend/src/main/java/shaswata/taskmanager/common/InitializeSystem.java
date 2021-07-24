package shaswata.taskmanager.common;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import shaswata.taskmanager.model.AdminAccount;
import shaswata.taskmanager.repository.hibernate.AdminDAO;


@Component
@RequiredArgsConstructor
public class InitializeSystem implements InitializingBean {

    @Value("${rootAdmin.email}")
    private String rootEmail;

    @Value("${rootAdmin.password}")
    private String rootPassword;


    private final AdminDAO adminRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void afterPropertiesSet() throws Exception {
        //call methods that initialize system
        initializeRootAdmin();
    }


    private void initializeRootAdmin(){
        if (adminRepo.findAdminAccountByEmail(rootEmail) == null) {
            AdminAccount rootAdmin = new AdminAccount();
            rootAdmin.setEmail(rootEmail);
            rootAdmin.setPassword(passwordEncoder.encode(rootPassword));
            rootAdmin.setName("root");
            adminRepo.create(rootAdmin);
        }
    }

}
