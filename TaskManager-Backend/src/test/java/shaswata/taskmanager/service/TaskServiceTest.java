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
import shaswata.taskmanager.common.ApplicationUserRole;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.task.TaskServiceAdminImpl;
import shaswata.taskmanager.service.task.TaskServiceUserImpl;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;


@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskRepository taskRepo;
    @Mock
    ProjectRepository projectRepo;
    @Mock
    UserRepository userRepo;

    @InjectMocks
    TaskServiceUserImpl userService;
    @InjectMocks
    TaskServiceAdminImpl adminService;

    private static final String USER_NAME = "ABCD";
    private static final String USER_EMAIL = "someone@gmail.com";
    private static final String USER_PASSWORD = "fSHBlfsuesefd";

    private static final String PROJECT_NAME = "Task Manager";
    private static final Long PROJECT_ID = 1l;
    private static final String PROJECT2_NAME = "Job Manager";
    private static final Long PROJECT2_ID = 2l;
    private static final long currentTimeInMillis = System.currentTimeMillis();
    private static final Date pastDate = new Date(currentTimeInMillis - 86400000);

    private static final String TASK1_DESCRIPTION = "Create Backend";
    private static final TaskStatus TASK1_STATUS = TaskStatus.open;
    private static final Date TASK1_DUEDATE = pastDate;

    private static final String TASK2_DESCRIPTION = "Create frontend";
    private static final TaskStatus TASK2_STATUS = TaskStatus.open;
    private static final Long TASK2_ID = (long) 4443344;

    private static final String TASK3_DESCRIPTION = "Create mobile app";
    private static final TaskStatus TASK3_STATUS = TaskStatus.closed;
    private static final Long TASK3_ID = (long) 6677644;

    private final UserAccount USER = new UserAccount();
    private final Project PROJECT = new Project();
    private final Project PROJECT2 = new Project();
    private final Task TASK1 = new Task();
    private final Task TASK2 = new Task();
    private final Task TASK3 = new Task();


    private final UserDetails CURRENT_USER = User.builder()
            .username(USER_EMAIL)
            .password(USER_PASSWORD)
            .roles(ApplicationUserRole.USER.name())
            .build();



    @BeforeEach
    public void setMockOutputs(){

        lenient().when(taskRepo.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            List<Task> taskList = new ArrayList<>();

            TASK1.setProject(PROJECT);
            TASK1.setDescription(TASK1_DESCRIPTION);
            TASK1.setStatus(TASK1_STATUS);
            TASK1.setDueDate(TASK1_DUEDATE);
            taskList.add(TASK1);

            TASK2.setProject(PROJECT);
            TASK2.setDescription(TASK2_DESCRIPTION);
            TASK2.setStatus(TASK2_STATUS);
            TASK2.setDueDate(new Date(currentTimeInMillis + 86400000));
            TASK2.setId(TASK2_ID);
            taskList.add(TASK2);

            TASK3.setProject(PROJECT);
            TASK3.setDescription(TASK3_DESCRIPTION);
            TASK3.setStatus(TASK3_STATUS);
            TASK3.setId(TASK3_ID);
            taskList.add(TASK3);

            PROJECT.setTasks(taskList);
            return taskList;

        });


        lenient().when(userRepo.findUserAccountByEmail(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(USER_EMAIL)) {

                List<Task> taskList = new ArrayList<>();
                List<Task> userTaskList = new ArrayList<>();
                taskList.add(TASK1);
                taskList.add(TASK2);
                userTaskList.add(TASK1);
                userTaskList.add(TASK2);
                List<Project> projectList = new ArrayList<>();
                projectList.add(PROJECT);

                TASK1.setStatus(TASK1_STATUS);
                TASK1.setDescription(TASK1_DESCRIPTION);
                TASK1.setProject(PROJECT);

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


        lenient().when(taskRepo.findTaskByProject(PROJECT)).thenAnswer((InvocationOnMock invocation) -> {
            List<Task> taskList = new ArrayList<>();

            TASK1.setProject(PROJECT);
            TASK1.setDescription(TASK1_DESCRIPTION);
            TASK1.setStatus(TASK1_STATUS);
            taskList.add(TASK1);

            TASK2.setProject(PROJECT);
            TASK2.setDescription(TASK2_DESCRIPTION);
            TASK2.setStatus(TASK2_STATUS);
            taskList.add(TASK2);

            PROJECT.setName(PROJECT_NAME);
            PROJECT.setTasks(taskList);
            return taskList;

        });

        lenient().when(taskRepo.findTaskById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(TASK2_ID)) {

                List<Task> taskList = new ArrayList<>();

                TASK2.setProject(PROJECT);
                TASK2.setDescription(TASK2_DESCRIPTION);
                TASK2.setStatus(TASK2_STATUS);
                TASK2.setId(TASK2_ID);
                taskList.add(TASK2);

                PROJECT.setName(PROJECT_NAME);
                PROJECT.setId(PROJECT_ID);
                PROJECT.setTasks(taskList);
                return TASK2;

            } else if(invocation.getArgument(0).equals(TASK3_ID)) {

                List<Task> taskList = new ArrayList<>();

                TASK3.setProject(PROJECT);
                TASK3.setDescription(TASK3_DESCRIPTION);
                TASK3.setStatus(TASK3_STATUS);
                TASK3.setId(TASK3_ID);
                taskList.add(TASK3);

                PROJECT.setName(PROJECT_NAME);
                PROJECT.setTasks(taskList);
                return TASK3;

            } else {
                return null;
            }

        });


        lenient().when(taskRepo.findTaskByStatus(any(TaskStatus.class))).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(TaskStatus.open)) {

                List<Task> taskList = new ArrayList<>();

                TASK1.setProject(PROJECT);
                TASK1.setDescription(TASK1_DESCRIPTION);
                TASK1.setStatus(TASK1_STATUS);
                taskList.add(TASK1);

                TASK2.setProject(PROJECT);
                TASK2.setDescription(TASK2_DESCRIPTION);
                TASK2.setStatus(TASK2_STATUS);
                taskList.add(TASK2);

                PROJECT.setName(PROJECT_NAME);
                PROJECT.setTasks(taskList);
                return taskList;

            } else if(invocation.getArgument(0).equals(TaskStatus.closed)) {

                List<Task> taskList = new ArrayList<>();

                TASK3.setProject(PROJECT);
                TASK3.setDescription(TASK3_DESCRIPTION);
                TASK3.setStatus(TASK3_STATUS);
                taskList.add(TASK3);

                PROJECT.setName(PROJECT_NAME);
                PROJECT.setTasks(taskList);
                return taskList;

            } else {
                return null;
            }

        });


        lenient().when(projectRepo.findProjectById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(PROJECT_ID)) {

                List<Task> taskList = new ArrayList<>();

                TASK1.setProject(PROJECT);
                TASK1.setDescription(TASK1_DESCRIPTION);
                TASK1.setStatus(TASK1_STATUS);
                TASK1.setDueDate(TASK1_DUEDATE);
                taskList.add(TASK1);

                PROJECT.setName(PROJECT_NAME);
                PROJECT.setId(PROJECT_ID);
                PROJECT.setTasks(taskList);
                return PROJECT;

            } else if (invocation.getArgument(0).equals(PROJECT2_ID)) {
                List<Task> taskList = new ArrayList<>();

                PROJECT2.setName(PROJECT2_NAME);
                PROJECT2.setId(PROJECT2_ID);
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
    public void testCreateTask(){

        TaskDto dto = new TaskDto();
        dto.setProjectId(PROJECT_ID);
        dto.setStatus(TaskStatus.open);
        dto.setDescription("Create Mobile App");

        try{
            dto = userService.createTask(dto, CURRENT_USER);
            assertEquals(PROJECT_ID, dto.getProjectId());
            assertEquals("Create Mobile App", dto.getDescription());
            assertEquals(TaskStatus.open, dto.getStatus());
            assertNull(dto.getDueDate());

        }catch (Exception e){
            fail(e.getMessage());
        }

    }


    @Test
    public void testCreateTaskNullDescription(){
        TaskDto dto = new TaskDto();
        dto.setProjectId(PROJECT_ID);
        dto.setStatus(TaskStatus.open);
        dto.setDescription(null);

        try{
            userService.createTask(dto, CURRENT_USER);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("Task description, status or project name cannot be empty!", e.getMessage());
        }
    }


    @Test
    public void testCreateTaskInvalidAccess(){
        TaskDto dto = new TaskDto();
        dto.setProjectId(PROJECT2_ID);
        dto.setStatus(TaskStatus.open);
        dto.setDescription(null);

        try{
            userService.createTask(dto, CURRENT_USER);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("Task description, status or project name cannot be empty!", e.getMessage());
        }
    }



    @Test
    public void testCreateTaskAlreadyInProject(){
        TaskDto dto = new TaskDto();
        dto.setProjectId(PROJECT_ID);
        dto.setStatus(TaskStatus.open);
        dto.setDescription(TASK1_DESCRIPTION);

        try{
            userService.createTask(dto, CURRENT_USER);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("Task already exists in project", e.getMessage());
        }
    }


    @Test
    public void testCreateTaskNonExistentProject(){
        TaskDto dto = new TaskDto();
        dto.setProjectId(3232L);
        dto.setStatus(TaskStatus.open);
        dto.setDescription("Create mobile app");

        try{
            userService.createTask(dto, CURRENT_USER);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("A task can only be created in an existing project!", e.getMessage());
        }

    }


    @Test
    public void testEditTask(){

        TaskDto dto = new TaskDto();
        dto.setProjectId(PROJECT_ID);
        dto.setStatus(TaskStatus.inProgress);
        dto.setDescription("Create mobile app");

        try{
            dto = userService.editTask(TASK2_ID, dto, CURRENT_USER);
            assertEquals("Create mobile app", dto.getDescription());
            assertEquals(TaskStatus.inProgress, dto.getStatus());
            assertEquals(PROJECT_ID, dto.getProjectId());
        }catch (Exception e){
            fail(e.getMessage());
        }

    }


    @Test
    public void testEditTaskNullID(){

        TaskDto dto = new TaskDto();
        dto.setProjectId(PROJECT_ID);
        dto.setStatus(TaskStatus.open);
        dto.setDescription(TASK1_DESCRIPTION);

        try{
            userService.editTask(null, dto, CURRENT_USER);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("Task ID cannot be empty!", e.getMessage());
        }

    }


    @Test
    public void testEditNonExistentTask(){

        TaskDto dto = new TaskDto();
        dto.setProjectId(PROJECT_ID);
        dto.setStatus(TaskStatus.open);
        dto.setDescription(TASK1_DESCRIPTION);

        try{
            userService.editTask((long)9999999, dto, CURRENT_USER);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("Task with given ID not found", e.getMessage());
        }

    }


    @Test
    public void testEditClosedTask(){
        TaskDto dto = new TaskDto();
        dto.setProjectId(PROJECT_ID);
        dto.setStatus(TaskStatus.inProgress);
        dto.setDescription("Do something");

        try{
            adminService.editTask(TASK3_ID, dto, CURRENT_USER);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("Task has already been closed so it cannot be updated", e.getMessage());
        }
    }



    @Test
    public void testGetTask(){
        try{
            TaskDto dto = adminService.getTask(TASK2_ID, CURRENT_USER);
            assertEquals(TASK2_DESCRIPTION, dto.getDescription());
            assertEquals(TASK2_STATUS, dto.getStatus());
            assertEquals(PROJECT_ID, dto.getProjectId());
        } catch(Exception e){
            fail(e.getMessage());
        }
    }


    @Test
    public void testGetTaskInvalidAccess(){
        try{
            TaskDto dto = userService.getTask(TASK3_ID, CURRENT_USER);
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Not a valid user of this task", e.getMessage());
        }
    }


    @Test
    public void testGetTaskNullID(){
        try{
            TaskDto dto = adminService.getTask(null, CURRENT_USER);
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Task ID cannot be empty!", e.getMessage());
        }
    }



    @Test
    public void testGetNonExistentTask(){
        try{
            TaskDto dto = adminService.getTask(101010101L, CURRENT_USER);
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Task with given ID not found", e.getMessage());
        }
    }



    @Test
    public void testGetAllTasks(){
        try{
            List<TaskDto> tasks = adminService.getAllTasks(CURRENT_USER);
            assertEquals(3, tasks.size());
            assertEquals(TASK1_DESCRIPTION, tasks.get(0).getDescription());
            assertEquals(TASK2_DESCRIPTION, tasks.get(1).getDescription());
            assertEquals(TASK3_DESCRIPTION, tasks.get(2).getDescription());
        } catch(Exception e){
            fail(e.getMessage());
        }
    }


    @Test
    public void testGetAllTasksUser(){
        try{
            List<TaskDto> tasks = userService.getAllTasks(CURRENT_USER);
            assertEquals(2, tasks.size());
            assertEquals(TASK1_DESCRIPTION, tasks.get(0).getDescription());
            assertEquals(TASK2_DESCRIPTION, tasks.get(1).getDescription());
        } catch(Exception e){
            fail(e.getMessage());
        }
    }



    @Test
    public void testGetTaskByProject(){

        try{
            List<TaskDto> tasks = adminService.getTasksByProject(PROJECT_ID, CURRENT_USER);
            assertEquals(2, tasks.size());
            assertEquals(TASK1_DESCRIPTION, tasks.get(0).getDescription());
            assertEquals(TASK2_DESCRIPTION, tasks.get(1).getDescription());
            assertEquals(PROJECT_ID, tasks.get(0).getProjectId());
        } catch(Exception e){
            fail(e.getMessage());
        }

    }


    @Test
    public void testGetTaskByNullProjectName(){
        try{
            List<TaskDto> tasks = adminService.getTasksByProject(null, CURRENT_USER);
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Project name cannot be empty!", e.getMessage());
        }
    }


    @Test
    public void testGetTaskByNonExistentProject(){
        try{
            List<TaskDto> tasks = adminService.getTasksByProject(669L, CURRENT_USER);
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Project with id '669' not found!", e.getMessage());
        }
    }


    @Test
    public void testGetTaskByOpenStatus(){
        try{
            List<TaskDto> dtos = adminService.getTasksByStatus("open", CURRENT_USER);
            assertEquals(2, dtos.size());
            assertEquals(TASK1_DESCRIPTION, dtos.get(0).getDescription());
            assertEquals(TASK1_STATUS, dtos.get(0).getStatus());
            assertEquals(TASK2_DESCRIPTION, dtos.get(1).getDescription());
            assertEquals(TASK2_STATUS, dtos.get(1).getStatus());
        } catch(Exception e){
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetTaskByClosedStatus(){
        try{
            List<TaskDto> dtos = adminService.getTasksByStatus("closed", CURRENT_USER);
            assertEquals(1, dtos.size());
            assertEquals(TASK3_DESCRIPTION, dtos.get(0).getDescription());
            assertEquals(TASK3_STATUS, dtos.get(0).getStatus());
        } catch(Exception e){
            fail(e.getMessage());
        }
    }


    @Test
    public void testGetTaskByNullStatus(){
        try{
            List<TaskDto> dtos = adminService.getTasksByStatus(null, CURRENT_USER);
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Task status cannot be empty!", e.getMessage());
        }
    }


    @Test
    public void testGetExpiredTasks(){
        try{
            List<TaskDto> tasks = adminService.getExpiredTasks(CURRENT_USER);
            assertEquals(1, tasks.size());
            assertEquals(TASK1_DESCRIPTION, tasks.get(0).getDescription());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }



}


