package shaswata.taskmanager.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import shaswata.taskmanager.dto.LoginDto;
import shaswata.taskmanager.model.AdminAccount;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.AdminRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.utilities.Role;
import shaswata.taskmanager.service.utilities.TokenProvider;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    private final String EMAIL = "bobby@gmail.com";
    private final String PASSWORD = "letmein123";
    private final String WRONG_EMAIL = "rick@gmail.com";


    @Mock
    UserRepository userRepo;
    @Mock
    AdminRepository adminRepo;
    @Mock
    TokenProvider tokenProvider;

    @InjectMocks
    AuthenticationService authenticationService;


    @BeforeEach
    public void setMockOutputs(){

        lenient().when(userRepo.findUserAccountByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(EMAIL)) {
                UserAccount user = new UserAccount();
                user.setEmail(EMAIL);
                user.setPassword(PASSWORD);
                return user;
            } else {
                throw new EntityNotFoundException();
            }
        });

        lenient().when(adminRepo.findAdminAccountByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(EMAIL)) {
                AdminAccount admin = new AdminAccount();
                admin.setEmail(EMAIL);
                admin.setPassword(PASSWORD);
                return admin;
            } else {
                throw new EntityNotFoundException();
            }
        });


    }



    @Test
    public void testValidAdminLogin() {
        try {
            LoginDto loginDto = new LoginDto();
            loginDto.setEmail(EMAIL);
            loginDto.setPassword(PASSWORD);
            loginDto.setRole(Role.Admin);
            authenticationService.logIn(loginDto);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testInvalidAdminLogin() {
        try{
            LoginDto loginDto = new LoginDto();
            loginDto.setEmail(WRONG_EMAIL);
            loginDto.setPassword(PASSWORD);
            loginDto.setRole(Role.Admin);
            authenticationService.logIn(loginDto);
            fail("Should throw exception");
        }catch(Exception e){
            assertTrue(true);
        }

    }



    @Test
    public void testValidUserLogin() {
        try {
            LoginDto loginDto = new LoginDto();
            loginDto.setEmail(EMAIL);
            loginDto.setPassword(PASSWORD);
            loginDto.setRole(Role.User);
            authenticationService.logIn(loginDto);
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testInvalidUserLogin() {
        try{
            LoginDto loginDto = new LoginDto();
            loginDto.setEmail(WRONG_EMAIL);
            loginDto.setPassword(PASSWORD);
            loginDto.setRole(Role.User);
            authenticationService.logIn(loginDto);
            fail("Should throw exception");
        }catch(Exception e){
            assertTrue(true);
        }

    }





}
