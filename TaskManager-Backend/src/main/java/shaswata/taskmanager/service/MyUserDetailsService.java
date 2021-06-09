package shaswata.taskmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.model.AdminAccount;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.AdminRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.security.ApplicationUserRole;


@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {


    private final UserRepository userRepository;
    private final AdminRepository adminRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //The person logged in can be either user or admin
        UserAccount user = userRepository.findUserAccountByEmail(username);
        AdminAccount admin = adminRepository.findAdminAccountByEmail(username);

        if(admin != null){
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
