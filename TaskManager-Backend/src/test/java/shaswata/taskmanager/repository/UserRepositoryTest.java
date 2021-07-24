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
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.hibernate.ProjectDAO;
import shaswata.taskmanager.repository.hibernate.TaskDAO;
import shaswata.taskmanager.repository.hibernate.UserDAO;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    UserDAO userRepo;

    @Autowired
    ProjectDAO projectRepo;

    @Autowired
    TaskDAO taskRepo;


    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        userRepo.deleteAll();
        taskRepo.deleteAll();
        projectRepo.deleteAll();
    }


    @Test
    public void testPersistAndLoadUser() {

        //create user
        String name = "New User";
        String email = "newUser@gmail.com";
        String password = "password123";

        UserAccount user = new UserAccount();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);

        //save to database
        userRepo.create(user);


        user = null;


        // load from database
        user = userRepo.findUserAccountByEmail(email);
        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals(name, user.getName());
        assertEquals(password, user.getPassword());


    }


    @Test
    public void testDeleteUser() {

        // create user
        String name = "New User";
        String email = "newUser@gmail.com";
        String password = "password123";

        UserAccount user = new UserAccount();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);

        // save user in database
        userRepo.create(user);

        user = null;
        user = userRepo.findUserAccountByEmail(email);
        assertNotNull(user);


        // delete from database
        user = null;
        userRepo.deleteUserAccountByEmail(email);

        assertNull(userRepo.findUserAccountByEmail(email));

    }



    @Test
    public void testPersistAndLoadUserTasks(){
        Project project = new Project();
        project.setName("TaskManager");
        projectRepo.create(project);
        List<Task> projectTasks = new ArrayList<>();
        project.setTasks(projectTasks);
        project = projectRepo.update(project);

        Task task1 = new Task();
        task1.setDescription("Create backend");
        task1.setStatus(TaskStatus.open);
        task1.setProject(project);
        taskRepo.create(task1);

        projectTasks = project.getTasks();
        projectTasks.add(task1);
        project.setTasks(projectTasks);
        projectRepo.update(project);

        //create user
        String name = "New User";
        String email = "newUser@gmail.com";
        String password = "password123";
        List<Task> userTasks = new ArrayList<Task>();
        List<Project> userProjects = new ArrayList<Project>();

        UserAccount user = new UserAccount();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);

        userTasks.add(task1);
        userProjects.add(project);
        user.setTasks(userTasks);
        user.setProjects(userProjects);

        userRepo.create(user);


        user = null;


        //Load from DB
        user = userRepo.findUserAccountByEmail(email);
        assertNotNull(user);
        assertEquals(email, user.getEmail());
        assertEquals("Create backend", user.getTasks().get(0).getDescription());
        assertEquals("TaskManager", user.getProjects().get(0).getName());

    }


    @Test
    public void testFindAllCustomers() {

        String userName1 = "TestUser1";
        String userEmail1 = "UserEmail1";
        String userPassword1 = "UserPassword1";

        UserAccount user1 = new UserAccount();

        user1.setName(userName1);
        user1.setEmail(userEmail1);
        user1.setPassword(userPassword1);
        userRepo.create(user1);


        String userName2 = "TestUser2";
        String userEmail2 = "UserEmail2";
        String userPassword2 = "UserPassword2";

        UserAccount user2 = new UserAccount();

        user2.setName(userName2);
        user2.setEmail(userEmail2);
        user2.setPassword(userPassword2);
        userRepo.create(user2);


        List<UserAccount> userList = userRepo.findAll();
        assertEquals(2, userList.size());
        assertEquals(userEmail1, userList.get(0).getEmail());
        assertEquals(userEmail2, userList.get(1).getEmail());


    }




}
