

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
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.security.ApplicationUserRole;
import shaswata.taskmanager.service.project.ProjectServiceAdminImpl;
import shaswata.taskmanager.service.project.ProjectServiceUserImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepo;
    @Mock
    TaskRepository taskRepo;
    @Mock
    UserRepository userRepo;

    @InjectMocks
    ProjectServiceAdminImpl adminService;
    @InjectMocks
    ProjectServiceUserImpl userService;

    private static final String USER_NAME = "ABCD";
    private static final String USER_EMAIL = "someone@gmail.com";
    private static final String USER_PASSWORD = "fSHBlfsuesefd";

    private static final String PROJECT1_NAME = "Task Manager";
    private static final String PROJECT2_NAME = "Another Project";


    private static final String TASK1_DESCRIPTION = "Create Backend";
    private static final TaskStatus TASK1_STATUS = TaskStatus.open;


    private static final String TASK2_DESCRIPTION = "Create frontend";
    private static final TaskStatus TASK2_STATUS = TaskStatus.open;


    private final UserAccount USER = new UserAccount();
    private final Project PROJECT1 = new Project();
    private final Project PROJECT2 = new Project();
    private final Task TASK1 = new Task();
    private final Task TASK2 = new Task();

    private final UserDetails CURRENT_USER = User.builder()
                                    .username(USER_EMAIL)
                                    .password(USER_PASSWORD)
                                    .roles(ApplicationUserRole.USER.name())
                                    .build();


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
    public void testCreateProject(){
        ProjectDto dto = new ProjectDto();
        dto.setName("New Project");
        List<TaskDto> taskDtoList = new ArrayList<>();

        TaskDto taskDto = new TaskDto();
        taskDto.setDescription("Create Website");
        taskDto.setProjectName("New Project");
        taskDto.setStatus(TaskStatus.open);
        taskDtoList.add(taskDto);

        dto.setTasks(taskDtoList);

        try {
            dto = userService.createProject(dto, CURRENT_USER);
            assertEquals("New Project", dto.getName());
            assertEquals("Create Website", dto.getTasks().get(0).getDescription());
            assertEquals("New Project", dto.getTasks().get(0).getProjectName());
            assertEquals(1, dto.getTasks().size());
        } catch (Exception e){
            fail(e.getMessage());
        }

    }


    @Test
    public void testCreateProjectNullName(){
        ProjectDto dto = new ProjectDto();
        dto.setName(null);
        List<TaskDto> taskDtoList = new ArrayList<>();
        dto.setTasks(taskDtoList);

        try {
            dto = userService.createProject(dto, CURRENT_USER);
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Project name cannot be empty!", e.getMessage());
        }
    }


    @Test
    public void testCreateProjectAlreadyExists(){
        ProjectDto dto = new ProjectDto();
        dto.setName(PROJECT1_NAME);
        List<TaskDto> taskDtoList = new ArrayList<>();
        dto.setTasks(taskDtoList);

        try {
            dto = userService.createProject(dto, CURRENT_USER);
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Project '" + PROJECT1_NAME + "' already exists!", e.getMessage());
        }
    }



    @Test
    public void testDeleteProject() {
        try {
            userService.deleteProject(PROJECT1_NAME, CURRENT_USER);
            verify(projectRepo, times(1)).deleteProjectByName(any());
            verify(userRepo, times(1)).findAll();

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void testDeleteProjectInvalidAccess() {
        try {
            userService.deleteProject(PROJECT2_NAME, CURRENT_USER);
            fail("Should throw exception");

        } catch (Exception e) {
            assertEquals("User can only delete own projects", e.getMessage());
        }
    }


    @Test
    public void testDeleteProjectNullName() {
        try {
            userService.deleteProject(null, CURRENT_USER);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("Project name cannot be empty!", e.getMessage());
        }
    }


    @Test
    public void testDeleteNonExistentProject() {
        try {
            userService.deleteProject("Some name", CURRENT_USER);
            fail("Should throw an exception");
        } catch (Exception e) {
            assertEquals("Project 'Some name' not found!", e.getMessage());
        }
    }


    @Test
    public void testGetAllProjectsUser(){
        try{
            List<ProjectDto> projectDtoList = userService.getAllProjects(CURRENT_USER);
            assertEquals(1, projectDtoList.size());
            assertEquals(PROJECT1_NAME, projectDtoList.get(0).getName());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetAllProjectsAdmin(){
        try{
            List<ProjectDto> projectDtoList = adminService.getAllProjects(CURRENT_USER);
            assertEquals(2, projectDtoList.size());
            assertEquals(PROJECT1_NAME, projectDtoList.get(0).getName());
            assertEquals(PROJECT2_NAME, projectDtoList.get(1).getName());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }







}


