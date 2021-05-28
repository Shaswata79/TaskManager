package shaswata.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.model.AdminAccount;
import shaswata.taskmanager.model.MyUserDetails;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.AdminRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.security.JwtUtil;

import java.util.ArrayList;


@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //The person logged in can be either user or admin
        UserAccount user = userRepository.findUserAccountByEmail(username);
        AdminAccount admin = adminRepository.findAdminAccountByEmail(username);

        if(admin != null){
            return new MyUserDetails(admin);
        } else if(user != null){
            return new MyUserDetails(user);
        } else{
            throw new UsernameNotFoundException("Account with email '" + username + "' not found.");
        }


    }


}
