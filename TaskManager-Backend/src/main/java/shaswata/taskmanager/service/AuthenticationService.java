
/*

package shaswata.taskmanager.service;

import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.LoginDto;
import shaswata.taskmanager.model.AdminAccount;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.AdminRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.utilities.TokenProvider;

import javax.transaction.Transactional;

@Service
public class AuthenticationService {

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    UserRepository userRepo;

    @Autowired
    AdminRepository adminRepo;


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Transactional
    public String logIn(LoginDto credentials) throws Exception {

        switch (credentials.getRole()) {
            case User:
                return authenticateUser(credentials.getEmail(), credentials.getPassword());
            case Admin:
                return authenticateAdmin(credentials.getEmail(), credentials.getPassword());
            default:
                throw new Exception("Invalid role.");
        }

    }


    private String authenticateUser(String email, String password) throws AuthenticationException {
        UserAccount user = userRepo.findUserAccountByEmail(email);
        if(user == null){
            throw new AuthenticationException("No user account exists with email " + email);
        }

        if (user.getPassword().equals(password)) {
            user.setToken(tokenProvider.createToken(user.getEmail()));
            return user.getToken();
        } else {
            throw new AuthenticationException("Invalid password.");
        }
    }


    private String authenticateAdmin(String email, String password) throws AuthenticationException {
        AdminAccount admin = adminRepo.findAdminAccountByEmail(email);
        if(admin == null){
            throw new AuthenticationException("No admin account exists with email " + email);
        }

        if (admin.getPassword().equals(password)) {
            admin.setToken(tokenProvider.createToken(admin.getEmail()));
            return admin.getToken();
        } else {
            throw new AuthenticationException("Invalid password.");
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Transactional
    public void logout(LoginDto credentials) throws Exception {
        switch (credentials.getRole()) {
            case User:
                logoutUser(credentials.getEmail());
                break;
            case Admin:
                logoutAdmin(credentials.getEmail());
                break;
            default:
                throw new Exception("Invalid role.");
        }
    }


    private void logoutUser(String email) {
        UserAccount user = userRepo.findUserAccountByEmail(email);
        user.setToken(null);
        userRepo.save(user);
    }

    private void logoutAdmin(String email) {
        AdminAccount admin = adminRepo.findAdminAccountByEmail(email);
        admin.setToken(null);
        adminRepo.save(admin);
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public AdminAccount validateAdminToken(String token) {
        return adminRepo.findAdminAccountByToken(token);
    }

    public UserAccount validateUserToken(String token) {
        return userRepo.findUserAccountByToken(token);
    }


}



 */