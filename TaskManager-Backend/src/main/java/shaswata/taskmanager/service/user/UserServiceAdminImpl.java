package shaswata.taskmanager.service.user;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.exception.DuplicateEntityException;
import shaswata.taskmanager.exception.InvalidInputException;
import shaswata.taskmanager.exception.ResourceNotFoundException;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.EmailService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceAdminImpl implements UserService {

    private final UserRepository userRepo;
    private final TaskRepository taskRepo;
    private final ProjectRepository projectRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Transactional
    @Override
    public UserDto createUser(UserDto dto) throws Exception {
        if(dto.getName() == null || dto.getName() == ""){
            throw new InvalidInputException("Name cannot be empty!");
        }
        if(dto.getEmail() == null || dto.getEmail() == "" || dto.getPassword() == null || dto.getPassword() == ""){
            throw new InvalidInputException("Email or password cannot be empty!");
        }

        if(userRepo.findUserAccountByEmail(dto.getEmail()) != null){
            throw new DuplicateEntityException("Account with email '" + dto.getEmail() + "' already exists.");
        }

        UserAccount user = new UserAccount();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        List<Task> taskList = new ArrayList<>();
        List<Project> projectList = new ArrayList<>();
        user.setTasks(taskList);
        user.setProjects(projectList);

        userRepo.save(user);
        return UserService.userToDTO(user);

    }



    @Transactional
    @Override
    public String assignUserToTask(String email, Long id, UserDetails currentUser) throws Exception {
        if(email == null || email == ""){
            throw new InvalidInputException("Email cannot be empty!");
        }
        if(id == null) {
            throw new InvalidInputException("Task ID cannot be empty!");
        }

        UserAccount user = userRepo.findUserAccountByEmail(email);
        if(user == null){
            throw new Exception("User account with email '" + email + "' not found.");
        }
        Task task = taskRepo.findTaskById(id);
        if(task == null){
            throw new ResourceNotFoundException("Task with ID:" + id + "' not found.");
        }

        List<Task> userTaskList = user.getTasks();
        if(userTaskList.contains(task)){
            throw new DuplicateEntityException("User is already assigned this task");
        }

        List<Project> userProjectList = user.getProjects();
        userTaskList.add(task);
        if(!userProjectList.contains(task.getProject())){
            userProjectList.add(task.getProject());
        }
        userRepo.save(user);
        return "User " + email + " assigned to task '" + task.getDescription() + "' in project '" + task.getProject().getName() + "'.";

    }


    @Transactional
    @Override
    public String assignUserToProject(String email, Long projectId, UserDetails currentUser) throws Exception {
        if(email == null || email == ""){
            throw new InvalidInputException("Email cannot be empty!");
        }
        if(projectId == null) {
            throw new InvalidInputException("Project id cannot be empty!");
        }

        UserAccount user = userRepo.findUserAccountByEmail(email);
        if(user == null){
            throw new Exception("User account with email '" + email + "' not found.");
        }
        Project project = projectRepo.findProjectById(projectId);
        if(project == null){
            throw new ResourceNotFoundException("Project with id '" + projectId + "' not found.");
        }

        List<Project> userProjectList = user.getProjects();
        if(userProjectList.contains(project)){
            throw new DuplicateEntityException("User is already assigned this project");
        }
        userProjectList.add(project);
        user.setProjects(userProjectList);
        userRepo.save(user);
        return "User " + email + " assigned to project '" + project.getName() + "' with id '" + project.getId() + "'.";

    }





    @Transactional
    @Override
    public UserDto getUser(String email, UserDetails currentUser) throws Exception {
        if(email == null || email == ""){
            throw new InvalidInputException("User email cannot be empty!");
        }
        UserAccount user = userRepo.findUserAccountByEmail(email);
        if(user == null){
            throw new Exception("No user account exists with email " + email);
        }

        return UserService.userToDTO(user);
    }

}
