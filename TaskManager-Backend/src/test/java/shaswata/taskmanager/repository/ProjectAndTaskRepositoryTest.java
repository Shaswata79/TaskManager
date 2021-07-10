package shaswata.taskmanager.repository;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class ProjectAndTaskRepositoryTest {

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    TaskRepository taskRepo;


    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        projectRepo.deleteAll();
        taskRepo.deleteAll();
    }


    @Test
    public void testPersistAndLoadProject(){

        Project project = new Project();
        project.setName("Task Manager");
        List<Task> taskList = new ArrayList<>();
        project.setTasks(taskList);
        project = projectRepo.save(project);
        Long projectID = project.getId();

        Task task1 = new Task();
        task1.setDescription("Create backend");
        task1.setStatus(TaskStatus.open);
        task1.setProject(project);
        taskRepo.save(task1);
        Long task1ID = task1.getId();

        Task task2 = new Task();
        task2.setDescription("Create frontend");
        task2.setStatus(TaskStatus.inProgress);
        task2.setProject(project);
        taskRepo.save(task2);

        taskList = project.getTasks();
        taskList.add(task1);
        taskList.add(task2);
        project.setTasks(taskList);
        projectRepo.save(project);


        project = null;
        task1 = null;
        task2 = null;


        //Load from Database
        project = projectRepo.findProjectById(projectID);
        assertNotNull(project);
        assertEquals("Task Manager", project.getName());
        assertEquals(2, project.getTasks().size());
        assertEquals("Create backend", project.getTasks().get(0).getDescription());
        assertEquals("Create frontend", project.getTasks().get(1).getDescription());

        task1 = taskRepo.findTaskById(task1ID);
        assertNotNull(task1);
        assertEquals("Create backend", task1.getDescription());

    }


    @Test
    public void testDeleteProject(){

        Project project = new Project();
        project.setName("Task Manager");
        project.setId(1L);
        List<Task> taskList = new ArrayList<>();
        project.setTasks(taskList);
        project = projectRepo.save(project);
        Long projectID = project.getId();

        Task task = new Task();
        task.setDescription("Create backend");
        task.setStatus(TaskStatus.open);
        task.setProject(project);
        task = taskRepo.save(task);
        Long taskID = task.getId();

        taskList = project.getTasks();
        taskList.add(task);
        project.setTasks(taskList);
        projectRepo.save(project);


        project = null;
        task = null;


        //Load from Database before delete
        project = projectRepo.findProjectById(projectID);
        assertNotNull(project);
        assertEquals("Task Manager", project.getName());
        assertEquals("Create backend", project.getTasks().get(0).getDescription());


        //delete project
        projectRepo.deleteProjectById(projectID);


        //try loading project from db
        project = projectRepo.findProjectById(projectID);
        assertNull(project);

        //deleting project should also delete tasks in project
        task = taskRepo.findTaskById(taskID);
        assertNull(task);

    }


    @Test
    public void testDeleteTask(){

        Project project = new Project();
        project.setName("Task Manager");
        project.setId(1L);
        List<Task> taskList = new ArrayList<>();
        project.setTasks(taskList);
        project = projectRepo.save(project);
        Long projectID = project.getId();

        Task task1 = new Task();
        task1.setDescription("Create backend");
        task1.setStatus(TaskStatus.open);
        task1.setProject(project);
        taskRepo.save(task1);


        Task task2 = new Task();
        task2.setDescription("Create frontend");
        task2.setStatus(TaskStatus.inProgress);
        task2.setProject(project);
        task2 = taskRepo.save(task2);
        Long task2ID = task2.getId();

        taskList = project.getTasks();
        taskList.add(task1);
        taskList.add(task2);
        project.setTasks(taskList);
        projectRepo.save(project);


        project = null;
        task1 = null;
        task2 = null;


        //Load from Database before delete
        project = projectRepo.findProjectById(projectID);
        assertNotNull(project);
        assertEquals("Task Manager", project.getName());
        assertEquals(2, project.getTasks().size());
        assertEquals("Create backend", project.getTasks().get(0).getDescription());
        assertEquals("Create frontend", project.getTasks().get(1).getDescription());

        task2 = taskRepo.findTaskById(task2ID);
        assertNotNull(task2);
        assertEquals("Create frontend", task2.getDescription());

        //delete a task
        List<Task> newTaskList = project.getTasks();
        newTaskList.remove(task2);      //for a task to be successfully removed all its references must be removed
        //project.setTasks(newTaskList);
        taskRepo.deleteById(task2ID);
        task2 = taskRepo.findTaskById(task2ID);
        assertNull(task2);


        project = projectRepo.findProjectById(projectID);
        assertNotNull(project);
        assertEquals("Task Manager", project.getName());
        assertEquals("Create backend", project.getTasks().get(0).getDescription());

    }


    @Test
    public void testFindTasksByProject(){

        Project project = new Project();
        project.setName("Task Manager");
        project.setId(1L);
        List<Task> taskList = new ArrayList<>();
        project.setTasks(taskList);
        project = projectRepo.save(project);

        Task task1 = new Task();
        task1.setDescription("Create backend");
        task1.setStatus(TaskStatus.open);
        task1.setProject(project);
        taskRepo.save(task1);


        Task task2 = new Task();
        task2.setDescription("Create frontend");
        task2.setStatus(TaskStatus.inProgress);
        task2.setProject(project);
        task2 = taskRepo.save(task2);
        Long task2ID = task2.getId();

        taskList = project.getTasks();
        taskList.add(task1);
        taskList.add(task2);
        project.setTasks(taskList);
        project = projectRepo.save(project);

        task1 = null;
        task2 = null;
        taskList = null;

        //find tasks from db using project name
        taskList = taskRepo.findTaskByProject(project);
        assertEquals(2, taskList.size());
        assertEquals("Create backend", taskList.get(0).getDescription());
        assertEquals("Create frontend", taskList.get(1).getDescription());

    }


    @Test
    public void testFindTasksByStatus(){


        Project project1 = new Project();
        project1.setName("Task Manager");
        List<Task> taskList1 = new ArrayList<>();
        project1.setTasks(taskList1);
        project1 = projectRepo.save(project1);

        Task task1 = new Task();
        task1.setDescription("Create backend");
        task1.setStatus(TaskStatus.open);
        task1.setProject(project1);
        taskRepo.save(task1);

        Task task2 = new Task();
        task2.setDescription("Create frontend");
        task2.setStatus(TaskStatus.inProgress);
        task2.setProject(project1);
        task2 = taskRepo.save(task2);
        Long task2ID = task2.getId();

        taskList1 = project1.getTasks();
        taskList1.add(task1);
        taskList1.add(task2);
        project1.setTasks(taskList1);
        project1 = projectRepo.save(project1);


        Project project2 = new Project();
        project2.setName("Mobile App");
        List<Task> taskList2 = new ArrayList<>();
        project2.setTasks(taskList2);
        project2 = projectRepo.save(project2);

        Task task3 = new Task();
        task3.setDescription("Create mobile frontend");
        task3.setStatus(TaskStatus.open);
        task3.setProject(project2);
        taskRepo.save(task3);

        taskList2 = project2.getTasks();
        taskList2.add(task3);
        project2.setTasks(taskList2);
        project2 = projectRepo.save(project2);

        task1 = null;
        task2 = null;
        task3 = null;
        taskList1 = null;
        taskList2 = null;

        //find tasks from db by task status
        taskList1 = taskRepo.findTaskByStatus(TaskStatus.open);
        assertEquals(2, taskList1.size());
        assertEquals("Create backend", taskList1.get(0).getDescription());
        assertEquals("Create mobile frontend", taskList1.get(1).getDescription());

    }

}


