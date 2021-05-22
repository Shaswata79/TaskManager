package shaswata.taskmanager.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    TaskRepository taskRepo;
    @Mock
    ProjectRepository projectRepo;

    @InjectMocks
    TaskService service;

    private static final String PROJECT_NAME = "Task Manager";
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

    private final Project PROJECT = new Project();
    private final Task TASK1 = new Task();
    private final Task TASK2 = new Task();
    private final Task TASK3 = new Task();

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

            PROJECT.setTasks(taskList);
            return taskList;

        });

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

            PROJECT.setTasks(taskList);
            return taskList;

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


        lenient().when(projectRepo.findProjectByName(anyString())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(PROJECT_NAME)) {

                List<Task> taskList = new ArrayList<>();

                TASK1.setProject(PROJECT);
                TASK1.setDescription(TASK1_DESCRIPTION);
                TASK1.setStatus(TASK1_STATUS);
                TASK1.setDueDate(TASK1_DUEDATE);
                taskList.add(TASK1);

                PROJECT.setName(PROJECT_NAME);
                PROJECT.setTasks(taskList);
                return PROJECT;

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
        dto.setProjectName(PROJECT_NAME);
        dto.setStatus(TaskStatus.open);
        dto.setDescription("Create Mobile App");

        try{
            dto = service.createTask(dto);
            assertEquals(PROJECT_NAME, dto.getProjectName());
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
        dto.setProjectName(PROJECT_NAME);
        dto.setStatus(TaskStatus.open);
        dto.setDescription(null);

        try{
            service.createTask(dto);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("Task description, status or project name cannot be empty!", e.getMessage());
        }
    }


    @Test
    public void testCreateTaskAlreadyInProject(){
        TaskDto dto = new TaskDto();
        dto.setProjectName(PROJECT_NAME);
        dto.setStatus(TaskStatus.open);
        dto.setDescription(TASK1_DESCRIPTION);

        try{
            service.createTask(dto);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("Task already exists in project", e.getMessage());
        }
    }


    @Test
    public void testCreateTaskNonExistentProject(){
        TaskDto dto = new TaskDto();
        dto.setProjectName("Imaginary Project");
        dto.setStatus(TaskStatus.open);
        dto.setDescription("Create mobile app");

        try{
            service.createTask(dto);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("A task can only be created in an existing project!", e.getMessage());
        }

    }


    @Test
    public void testEditTask(){

        TaskDto dto = new TaskDto();
        dto.setProjectName(PROJECT_NAME);
        dto.setStatus(TaskStatus.inProgress);
        dto.setDescription("Create mobile app");

        try{
            dto = service.editTask(TASK2_ID, dto);
            assertEquals("Create mobile app", dto.getDescription());
            assertEquals(TaskStatus.inProgress, dto.getStatus());
            assertEquals(PROJECT_NAME, dto.getProjectName());
        }catch (Exception e){
            fail(e.getMessage());
        }

    }


    @Test
    public void testEditTaskNullID(){

        TaskDto dto = new TaskDto();
        dto.setProjectName(PROJECT_NAME);
        dto.setStatus(TaskStatus.open);
        dto.setDescription(TASK1_DESCRIPTION);

        try{
            service.editTask(null, dto);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("Task ID cannot be empty!", e.getMessage());
        }

    }


    @Test
    public void testEditNonExistentTask(){

        TaskDto dto = new TaskDto();
        dto.setProjectName(PROJECT_NAME);
        dto.setStatus(TaskStatus.open);
        dto.setDescription(TASK1_DESCRIPTION);

        try{
            service.editTask((long)9999999, dto);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("Task with given ID not found", e.getMessage());
        }

    }


    @Test
    public void testEditClosedTask(){
        TaskDto dto = new TaskDto();
        dto.setProjectName(PROJECT_NAME);
        dto.setStatus(TaskStatus.inProgress);
        dto.setDescription("Do something");

        try{
            service.editTask(TASK3_ID, dto);
            fail("Must throw exception");
        }catch (Exception e){
            assertEquals("Task has already been closed so it cannot be updated", e.getMessage());
        }
    }



    @Test
    public void testGetTask(){
        try{
            TaskDto dto = service.getTask(TASK2_ID);
            assertEquals(TASK2_DESCRIPTION, dto.getDescription());
            assertEquals(TASK2_STATUS, dto.getStatus());
            assertEquals(PROJECT_NAME, dto.getProjectName());
        } catch(Exception e){
            fail(e.getMessage());
        }
    }


    @Test
    public void testGetTaskNullID(){
        try{
            TaskDto dto = service.getTask(null);
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Task ID cannot be empty!", e.getMessage());
        }
    }



    @Test
    public void testGetNonExistentTask(){
        try{
            TaskDto dto = service.getTask(101010101L);
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Task with given ID not found", e.getMessage());
        }
    }



    @Test
    public void testGetAllTasks(){
        try{
            List<TaskDto> tasks = service.getAllTasks();
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
            List<TaskDto> tasks = service.getTasksByProject(PROJECT_NAME);
            assertEquals(2, tasks.size());
            assertEquals(TASK1_DESCRIPTION, tasks.get(0).getDescription());
            assertEquals(TASK2_DESCRIPTION, tasks.get(1).getDescription());
            assertEquals(PROJECT_NAME, tasks.get(0).getProjectName());
        } catch(Exception e){
            fail(e.getMessage());
        }

    }


    @Test
    public void testGetTaskByNullProjectName(){
        try{
            List<TaskDto> tasks = service.getTasksByProject(null);
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Project name cannot be empty!", e.getMessage());
        }
    }


    @Test
    public void testGetTaskByNonExistentProject(){
        try{
            List<TaskDto> tasks = service.getTasksByProject("Imaginary Project");
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Project 'Imaginary Project' not found!", e.getMessage());
        }
    }


    @Test
    public void testGetTaskByOpenStatus(){
        try{
            List<TaskDto> dtos = service.getTasksByStatus(TaskStatus.open);
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
            List<TaskDto> dtos = service.getTasksByStatus(TaskStatus.closed);
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
            List<TaskDto> dtos = service.getTasksByStatus(null);
            fail("Should throw exception");
        } catch(Exception e){
            assertEquals("Task status cannot be empty!", e.getMessage());
        }
    }


    @Test
    public void testGetExpiredTasks(){
        try{
            List<TaskDto> tasks = service.getExpiredTasks();
            assertEquals(1, tasks.size());
            assertEquals(TASK1_DESCRIPTION, tasks.get(0).getDescription());
        } catch (Exception e){
            fail(e.getMessage());
        }
    }



}
