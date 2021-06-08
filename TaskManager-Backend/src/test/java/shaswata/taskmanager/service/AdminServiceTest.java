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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    ProjectRepository projectRepo;
    @Mock
    TaskRepository taskRepo;
    @Mock
    UserRepository userRepo;

    @InjectMocks
    AdminService adminService;


    private static final String USER_NAME = "ABCD";
    private static final String USER_EMAIL = "someone@gmail.com";
    private static final String USER_PASSWORD = "fSHBlfsuesefd";

    private static final String USER2_NAME = "ABCD";
    private static final String USER2_EMAIL = "someone@gmail.com";
    private static final String USER2_PASSWORD = "fSHBlfsuesefd";

    private static final String PROJECT1_NAME = "Task Manager";
    private static final String PROJECT2_NAME = "Another Project";


    private static final String TASK1_DESCRIPTION = "Create Backend";
    private static final TaskStatus TASK1_STATUS = TaskStatus.open;


    private static final String TASK2_DESCRIPTION = "Create frontend";
    private static final TaskStatus TASK2_STATUS = TaskStatus.open;


    private final UserAccount USER = new UserAccount();
    private final UserAccount USER2 = new UserAccount();
    private final Project PROJECT1 = new Project();
    private final Project PROJECT2 = new Project();
    private final Task TASK1 = new Task();
    private final Task TASK2 = new Task();


    @BeforeEach
    public void setMockOutputs(){

        lenient().when(projectRepo.findAll()).thenAnswer((InvocationOnMock invocation) -> {

            List<Task> taskList1 = new ArrayList<>();
            List<Task> taskList2 = new ArrayList<>();

            TASK1.setProject(PROJECT1);
            TASK1.setDescription(TASK1_DESCRIPTION);
            TASK1.setStatus(TASK1_STATUS);
            taskList1.add(TASK1);

            TASK2.setProject(PROJECT2);
            TASK2.setDescription(TASK2_DESCRIPTION);
            TASK2.setStatus(TASK2_STATUS);
            taskList2.add(TASK2);

            PROJECT1.setName(PROJECT1_NAME);
            PROJECT2.setName(PROJECT2_NAME);
            PROJECT1.setTasks(taskList1);
            PROJECT2.setTasks(taskList2);

            List<Project> projectList = new ArrayList<>();
            projectList.add(PROJECT1);
            projectList.add(PROJECT2);
            return projectList;

        });


        lenient().when(userRepo.findAll()).thenAnswer((InvocationOnMock invocation) -> {

            List<UserAccount> userList = new ArrayList<>();

            USER.setEmail(USER_EMAIL);
            USER.setPassword(USER_PASSWORD);
            USER.setName(USER_NAME);
            USER.setTasks(new ArrayList<Task>());
            USER.setProjects(new ArrayList<Project>());

            USER2.setEmail(USER2_EMAIL);
            USER2.setPassword(USER2_PASSWORD);
            USER2.setName(USER2_NAME);
            USER2.setTasks(new ArrayList<Task>());
            USER2.setProjects(new ArrayList<Project>());

            userList.add(USER);
            userList.add(USER2);

            return userList;

        });


        lenient().when(userRepo.findUserAccountByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(USER_EMAIL)) {

                List<Task> taskList = new ArrayList<>();
                List<Task> userTaskList = new ArrayList<>();
                taskList.add(TASK1);
                taskList.add(TASK2);
                userTaskList.add(TASK1);
                List<Project> projectList = new ArrayList<>();
                projectList.add(PROJECT1);

                TASK1.setStatus(TASK1_STATUS);
                TASK1.setDescription(TASK1_DESCRIPTION);
                TASK1.setProject(PROJECT1);
                TASK2.setDescription(TASK2_DESCRIPTION);
                TASK2.setStatus(TASK2_STATUS);
                TASK2.setProject(PROJECT1);

                PROJECT1.setTasks(taskList);
                PROJECT1.setName(PROJECT1_NAME);

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



        lenient().when(projectRepo.findProjectByName(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(PROJECT1_NAME)) {
                List<Task> taskList = new ArrayList<>();

                TASK1.setProject(PROJECT1);
                TASK1.setDescription(TASK1_DESCRIPTION);
                TASK1.setStatus(TASK1_STATUS);
                taskList.add(TASK1);

                PROJECT1.setName(PROJECT1_NAME);
                PROJECT1.setTasks(taskList);
                return PROJECT1;

            } else if(invocation.getArgument(0).equals(PROJECT2_NAME)){
                List<Task> taskList = new ArrayList<>();

                TASK2.setProject(PROJECT2);
                TASK2.setDescription(TASK2_DESCRIPTION);
                TASK2.setStatus(TASK2_STATUS);
                taskList.add(TASK2);

                PROJECT2.setName(PROJECT2_NAME);
                PROJECT2.setTasks(taskList);
                return PROJECT2;

            } else {
                return null;
            }

        });


        lenient().when(taskRepo.save(any(Task.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
        lenient().when(projectRepo.save(any(Project.class))).thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    }

    @Test
    public void testGetAllProjectsByUser(){

        try {
            List<ProjectDto> projectDtoList = adminService.getProjectsByUser(USER_EMAIL);
            assertEquals(1, projectDtoList.size());
            assertEquals(PROJECT1_NAME, projectDtoList.get(0).getName());
        } catch (Exception e){
            fail(e.getMessage());
        }

    }



    @Test
    public void testGetAllTasksByUser(){

        try {
            List<TaskDto> taskDtoList = adminService.getTasksByUser(USER_EMAIL);
            assertEquals(1, taskDtoList.size());
            assertEquals(TASK1_DESCRIPTION, taskDtoList.get(0).getDescription());
        } catch (Exception e){
            fail(e.getMessage());
        }

    }



    @Test
    public void testGetAllUsers(){
        try {
            List<UserDto> userDtoList = adminService.getAllUsers();
            assertEquals(2, userDtoList.size());
            assertEquals(USER_EMAIL, userDtoList.get(0).getEmail());
            assertEquals(USER2_EMAIL, userDtoList.get(1).getEmail());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }
}
