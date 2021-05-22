package shaswata.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.AdminRepository;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepo;

    @Autowired
    TaskRepository taskRepo;


    @Transactional
    public UserDto createUser(UserDto dto) throws Exception {
        if(dto.getName() == null){
            throw new Exception("Name cannot be empty!");
        }
        if(dto.getEmail() == null || dto.getPassword() == null){
            throw new Exception("Email or password cannot be empty!");
        }

        if(userRepo.findUserAccountByEmail(dto.getEmail()) != null){
            throw new Exception("Account with email '" + dto.getEmail() + "' already exists.");
        }

        UserAccount user = new UserAccount();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        List<Task> taskList = new ArrayList<>();
        List<Project> projectList = new ArrayList<>();
        user.setTasks(taskList);
        user.setProjects(projectList);

        userRepo.save(user);
        return userToDTO(user);

    }


    @Transactional
    public UserDto changePassword(String email, String newPassword) throws Exception {
        if(email == null || newPassword == null){
            throw new Exception("Email or new password cannot be empty!");
        }

        UserAccount user = userRepo.findUserAccountByEmail(email);
        if(user == null){
            throw new Exception("User account with email '" + email + "' not found.");
        }

        user.setPassword(newPassword);
        userRepo.save(user);
        return userToDTO(user);

    }


    @Transactional
    public String assignUserToTask(String email, Long id) throws Exception {
        if(email == null){
            throw new Exception("Email cannot be empty!");
        }
        if(id == null) {
            throw new Exception("Task ID cannot be empty!");
        }

        UserAccount user = userRepo.findUserAccountByEmail(email);
        if(user == null){
            throw new Exception("User account with email '" + email + "' not found.");
        }
        Task task = taskRepo.findTaskById(id);
        if(task == null){
            throw new Exception("Task with ID:" + id + "' not found.");
        }

        List<Task> userTaskList = user.getTasks();
        List<Project> userProjectList = user.getProjects();
        userTaskList.add(task);
        user.setTasks(userTaskList);
        if(!userProjectList.contains(task.getProject())){
            userProjectList.add(task.getProject());
            user.setProjects(userProjectList);
        }
        userRepo.save(user);
        return "User " + email + " assigned to task '" + task.getDescription() + "' in project '" + task.getProject().getName() + "'.";

    }



    @Transactional
    public List<UserDto> getAllUsers(){
        List<UserAccount> users = userRepo.findAll();
        List<UserDto> userDtos = new ArrayList<>();

        for(UserAccount user : users){
            userDtos.add(userToDTO(user));
        }

        return userDtos;
    }



    @Transactional
    public List<TaskDto> getTasksByUser(String email) throws Exception {
        if(email == null){
            throw new Exception("User email cannot be empty!");
        }
        UserAccount user = userRepo.findUserAccountByEmail(email);
        if(user == null){
            throw new Exception("No user account exists with email " + email);
        }

        List<Task> userTasks = user.getTasks();
        List<TaskDto> taskDtoList = new ArrayList<>();

        for(Task task : userTasks){
            taskDtoList.add(TaskService.taskToDTO(task));
        }

        return taskDtoList;

    }



    @Transactional
    public List<ProjectDto> getProjectsByUser(String email) throws Exception {
        if(email == null){
            throw new Exception("User email cannot be empty!");
        }
        UserAccount user = userRepo.findUserAccountByEmail(email);
        if(user == null){
            throw new Exception("No user account exists with email " + email);
        }

        List<Project> userProjects = user.getProjects();
        List<ProjectDto> projectDtoList = new ArrayList<>();

        for(Project project : userProjects){
            projectDtoList.add(ProjectService.projectToDTO(project));
        }

        return projectDtoList;

    }

    @Transactional
    public UserDto getUser(String email) throws Exception {
        if(email == null){
            throw new Exception("User email cannot be empty!");
        }
        UserAccount user = userRepo.findUserAccountByEmail(email);
        if(user == null){
            throw new Exception("No user account exists with email " + email);
        }

        return userToDTO(user);
    }


    public static UserDto userToDTO(UserAccount user) {
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPassword(user.getPassword());
        dto.setToken(user.getToken());
        return dto;
    }


}
