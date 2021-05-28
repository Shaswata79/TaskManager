package shaswata.taskmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepo;
    @Mock
    TaskRepository taskRepo;
    @Mock
    ProjectRepository projectRepo;

    @InjectMocks
    UserService service;


    private static final String USER_NAME = "ABCD";
    private static final String USER_EMAIL = "someone@gmail.com";
    private static final String USER_PASSWORD = "fSHBlfsuesefd";

    private static final String PROJECT_NAME = "Task Manager";
    private static final String TASK_DESCRIPTION = "Create Backend";
    private static final TaskStatus TASK_STATUS = TaskStatus.open;
    private static final String TASK2_DESCRIPTION = "Create frontend";
    private static final TaskStatus TASK2_STATUS = TaskStatus.open;
    private static final Long TASK2_ID = (long) 4443344;

    private final UserAccount USER = new UserAccount();
    private final Project PROJECT = new Project();
    private final Task TASK = new Task();
    private final Task TASK2 = new Task();





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
            } else {
                return null;
            }

        });


        lenient().when(taskRepo.findTaskById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(TASK2_ID)) {

                TASK2.setId(TASK2_ID);
                TASK2.setDescription(TASK2_DESCRIPTION);
                TASK2.setStatus(TASK2_STATUS);
                TASK2.setProject(PROJECT);
                return TASK2;

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
            user = service.createUser(user);
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

        String userName = "ABCD";
        String userEmail = null;
        String userPassword = null;

        UserDto user = new UserDto();
        user.setPassword(userPassword);
        user.setEmail(userEmail);
        user.setName(userName);

        try {
            service.createUser(user);
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

            service.createUser(user);
            fail();

        } catch (Exception e) {
            //an error should occur
            assertEquals("Account with email '" + USER_EMAIL + "' already exists.", e.getMessage());
        }

    }

/*

    @Test
    public void testChangePassword() {

        String newPassword = "gsdfjsoifkl";
        UserDto user = null;

        try {
            user = service.changePassword(USER_EMAIL, newPassword);
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
            service.changePassword(null, null);
            fail();
        } catch (Exception e) {
            //error should occur
            assertEquals("Email or new password cannot be empty!", e.getMessage());
        }
    }


    @Test
    public void testChangePasswordNonExistentUser() {

        try {
            service.changePassword("someMail@gmail.com", "fdjhyffaskd");
            fail();
        } catch (Exception e) {
            //error should occur
            assertEquals("User account with email 'someMail@gmail.com' not found.", e.getMessage());
        }
    }



 */

    @Test
    public void testGetUser() {

        UserDto user = null;

        try {
            user = service.getUser(USER_EMAIL);
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
            service.getUser(null);
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
            service.getUser(userEmail);
            fail();
        } catch (Exception e) {
            // error should occur
            assertEquals("No user account exists with email " + userEmail, e.getMessage());
        }

    }


    @Test
    public void testGetAllUsers() {

        try {
            List<UserDto> users = service.getAllUsers();
            assertTrue(users.stream().map(UserDto::getEmail).collect(Collectors.toList()).contains(USER_EMAIL));
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }



    @Test
    public void testGetTasksByUser(){

        try{
            List<TaskDto> tasks = service.getTasksByUser(USER_EMAIL);
            assertEquals(1, tasks.size());
            assertEquals(TASK_DESCRIPTION, tasks.get(0).getDescription());
            assertEquals(TASK_STATUS, tasks.get(0).getStatus());
            assertEquals(PROJECT_NAME, tasks.get(0).getProjectName());

        }catch (Exception e){
            fail(e.getMessage());
        }
    }


    @Test
    public void testGetProjectsByUser(){

        try{
            List<ProjectDto> projects = service.getProjectsByUser(USER_EMAIL);
            assertEquals(projects.size(), 1);
            assertEquals(PROJECT_NAME, projects.get(0).getName());
            assertEquals(TASK_DESCRIPTION, projects.get(0).getTasks().get(0).getDescription());

        }catch (Exception e){
            fail(e.getMessage());
        }

    }


    @Test
    public void testAssignUserToTask(){

        String message = null;
        try{
            message  = service.assignUserToTask(USER_EMAIL, TASK2_ID);

        }catch (Exception e){
            fail(e.getMessage());
        }

        assertEquals("User " + USER_EMAIL + " assigned to task '" + TASK2_DESCRIPTION + "' in project '" + PROJECT_NAME + "'.", message);
        assertEquals(2, USER.getTasks().size());
        assertEquals(TASK2_DESCRIPTION, USER.getTasks().get(1).getDescription());

    }


}
