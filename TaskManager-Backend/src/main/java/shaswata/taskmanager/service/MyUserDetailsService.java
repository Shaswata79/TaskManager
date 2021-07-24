package shaswata.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.common.ApplicationUserRole;
import shaswata.taskmanager.model.AdminAccount;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.hibernate.AdminDAO;
import shaswata.taskmanager.repository.hibernate.UserDAO;


@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {


    private final UserDAO userRepository;
    private final AdminDAO adminRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //The person logged in can be either user or admin
        UserAccount user = userRepository.findUserAccountByEmail(username);
        AdminAccount admin = adminRepository.findAdminAccountByEmail(username);

        if(admin != null){
            System.out.println("Here MyUserDetailService");
            UserDetails currentAdmin = User.builder()
                    .username(admin.getEmail())
                    .password(admin.getPassword())
                    .roles(ApplicationUserRole.ADMIN.name())
                    .build();
            return currentAdmin;

        } else if(user != null){
            UserDetails currentUser = User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .roles(ApplicationUserRole.USER.name())
                    .build();
            return currentUser;

        } else{
            throw new UsernameNotFoundException("Account with email '" + username + "' not found.");
        }


    }


}
