package shaswata.taskmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.security.ApplicationUserRole;
import shaswata.taskmanager.service.user.UserServiceAdminImpl;
import shaswata.taskmanager.service.user.UserServiceUserImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepo;
    @Mock
    TaskRepository taskRepo;
    @Mock
    ProjectRepository projectRepo;

    @InjectMocks
    UserServiceUserImpl userService;
    @InjectMocks
    UserServiceAdminImpl adminService;


    private static final String USER_NAME = "ABCD";
    private static final String USER_EMAIL = "someone@gmail.com";
    private static final String USER_PASSWORD = "fSHBlfsuesefd";

    private static final String USER2_NAME = "EFGH";
    private static final String USER2_EMAIL = "someone123@gmail.com";
    private static final String USER2_PASSWORD = "fffdfdfdfdd";

    private static final String PROJECT_NAME = "Task Manager";
    private static final String TASK_DESCRIPTION = "Create Backend";
    private static final TaskStatus TASK_STATUS = TaskStatus.open;
    private static final String TASK2_DESCRIPTION = "Create frontend";
    private static final TaskStatus TASK2_STATUS = TaskStatus.open;
    private static final Long TASK2_ID = (long) 4443344;

    private final UserAccount USER = new UserAccount();
    private final UserAccount USER2 = new UserAccount();
    private final Project PROJECT = new Project();
    private final Task TASK = new Task();
    private final Task TASK2 = new Task();

    private final UserDetails CURRENT_USER = User.builder()
            .username(USER_EMAIL)
            .password(USER_PASSWORD)
            .roles(ApplicationUserRole.USER.name())
            .build();

    private final UserDetails DUMMY_ADMIN = User.builder()
            .username("admin")
            .password("admin")
            .roles(ApplicationUserRole.ADMIN.name())
            .build();



    @BeforeEach
    public void setMockOutputs(){

        lenient().when(userRepo.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            USER.setName(USER_NAME);
            USER.setEmail(USER_EMAIL);
            USER.setPassword(USER_PASSWORD);

            List<UserAccount> userList = new ArrayList<>();
            userList.add(USER);
            return userList;

        });

        lenient().when(userRepo.findUserAccountByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(USER_EMAIL)) {

                List<Task> taskList = new ArrayList<>();
                List<Task> userTaskList = new ArrayList<>();
                taskList.add(TASK);
                taskList.add(TASK2);
                userTaskList.add(TASK);
                List<Project> projectList = new ArrayList<>();
                projectList.add(PROJECT);

                TASK.setStatus(TASK_STATUS);
                TASK.setDescription(TASK_DESCRIPTION);
                TASK.setProject(PROJECT);
                TASK2.setId(TASK2_ID);
                TASK2.setDescription(TASK2_DESCRIPTION);
                TASK2.setStatus(TASK2_STATUS);
                TASK2.setProject(PROJECT);

                PROJECT.setTasks(taskList);
                PROJECT.setName(PROJECT_NAME);

                USER.setName(USER_NAME);
                USER.setPassword(USER_PASSWORD);
                USER.setEmail(USER_EMAIL);
                USER.setProjects(projectList);
                USER.setTasks(userTaskList);

                return USER;

            } else if (invocation.getArgument(0).equals(USER2_EMAIL)) {


                USER2.setName(USER2_NAME);
                USER2.setPassword(USER2_PASSWORD);
                USER2.setEmail(USER2_EMAIL);
                USER2.setProjects(new ArrayList<>());
                USER2.setTasks(new ArrayList<>());

                return USER2;

            } else {
                return null;
            }

        });


        lenient().when(taskRepo.findTaskById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(TASK2_ID)) {
                PROJECT.setName(PROJECT_NAME);

                TASK2.setId(TASK2_ID);
                TASK2.setDescription(TASK2_DESCRIPTION);
                TASK2.setStatus(TASK2_STATUS);
                TASK2.setProject(PROJECT);

                return TASK2;

            } else {
                return null;
            }

        });

        lenient().when(projectRepo.findProjectByName(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(PROJECT_NAME)) {
                PROJECT.setName(PROJECT_NAME);
                PROJECT.setTasks(new ArrayList<>());
                List<Project> projectList = new ArrayList<>();
                projectList.add(PROJECT);

                USER.setName(USER_NAME);
                USER.setPassword(USER_PASSWORD);
                USER.setEmail(USER_EMAIL);
                USER.setProjects(projectList);
                USER.setTasks(new ArrayList<>());

                return PROJECT;

            } else {
                return null;
            }

        });

        lenient().when(userRepo.save(any(UserAccount.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    }


    @Test
    public void testCreateUser() {

        String userName = "ABCD";
        String userEmail = "somebody@gmail.com";
        String userPassword = "fSHBlfsuesefd";

        UserDto user = new UserDto();
        user.setPassword(userPassword);
        user.setEmail(userEmail);
        user.setName(userName);

        try {
            user = userService.createUser(user);
        } catch (Exception e) {
            // Check that no error occurred
            fail(e.getMessage());
        }

        assertNotNull(user);
        assertEquals(userName, user.getName());
        assertEquals(userEmail, user.getEmail());

    }


    @Test
    public void testCreateUserNull() {

        UserDto user = new UserDto();
        user.setPassword(null);
        user.setEmail(null);
        user.setName("ABCD");

        try {
            userService.createUser(user);
            fail();
        } catch (Exception e) {
            //an error should occur
            assertEquals("Email or password cannot be empty!", e.getMessage());
        }


    }


    @Test
    public void testCreateUserDuplicate() {

        UserDto user = new UserDto();
        user.setPassword(USER_PASSWORD);
        user.setEmail(USER_EMAIL);
        user.setName(USER_NAME);

        try {

            userService.createUser(user);
            fail();

        } catch (Exception e) {
            //an error should occur
            assertEquals("Account with email '" + USER_EMAIL + "' already exists.", e.getMessage());
        }

    }



    @Test
    public void testChangePassword() {

        String newPassword = "gsdfjsoifkl";
        UserDto user = null;

        try {
            user = userService.changePassword(newPassword, CURRENT_USER);
        } catch (Exception e) {
            // Check that no error occurred
            fail(e.getMessage());
        }

        assertNotNull(user);
        assertEquals(newPassword, user.getPassword());
        assertEquals(USER_EMAIL, user.getEmail());

    }



    @Test
    public void testChangePasswordNull() {

        try {
            userService.changePassword(null, CURRENT_USER);
            fail();
        } catch (Exception e) {
            //error should occur
            assertEquals("New password cannot be empty!", e.getMessage());
        }
    }


    @Test
    public void testChangePasswordNonExistentUser() {
        UserDetails NO_USER = User.builder()
                .username("imaginary@gmail.com")
                .password("skufhsfsd")
                .roles(ApplicationUserRole.USER.name())
                .build();

        try {
            userService.changePassword("xxxxxxxxxxxxxxx", NO_USER);
            fail();
        } catch (Exception e) {
            //error should occur
            assertEquals("User account with email 'imaginary@gmail.com' not found.", e.getMessage());
        }
    }





    @Test
    public void testGetUser() {
        UserDto user = null;

        try {
            user = userService.getUser(USER_EMAIL, CURRENT_USER);
        } catch (Exception e) {
            // Check that no error occurred
            fail();
        }

        assertNotNull(user);
        assertEquals(USER_NAME, user.getName());
        assertEquals(USER_EMAIL, user.getEmail());
    }


    @Test
    public void testGetUserInvalidAccess() {
        UserDto user = null;

        try {
            user = userService.getUser(USER2_EMAIL, CURRENT_USER);
            fail("Should throw exception");
        } catch (Exception e) {
            assertEquals("You can only view your own account details!", e.getMessage());

        }

    }



    @Test
    public void testGetUserAdmin() {
        UserDto user = null;

        try {
            user = adminService.getUser(USER_EMAIL, DUMMY_ADMIN);
        } catch (Exception e) {
            // Check that no error occurred
            fail();
        }

        assertNotNull(user);
        assertEquals(USER_NAME, user.getName());
        assertEquals(USER_EMAIL, user.getEmail());
    }


    @Test
    public void testGetUserNull() {

        try {
            adminService.getUser(null, CURRENT_USER);
            fail();
        } catch (Exception e) {
            // error should occur
            assertEquals("User email cannot be empty!", e.getMessage());
        }

    }


    @Test
    public void testGetNonExistentUser() {

        String userEmail = "xyz@gmail.com";

        try {
            adminService.getUser(userEmail, DUMMY_ADMIN);
            fail();
        } catch (Exception e) {
            // error should occur
            assertEquals("No user account exists with email " + userEmail, e.getMessage());
        }

    }



    @Test
    public void testAssignUserToTask(){

        String message = null;
        try{
            message  = userService.assignUserToTask(USER2_EMAIL, TASK2_ID, CURRENT_USER);

        }catch (Exception e){
            fail(e.getMessage());
        }

        assertEquals("User " + USER2_EMAIL + " assigned to task '" + TASK2_DESCRIPTION + "' in project '" + PROJECT_NAME + "'.", message);
        assertEquals(1, USER2.getTasks().size());
        assertEquals(TASK2_DESCRIPTION, USER2.getTasks().get(0).getDescription());

    }


}

