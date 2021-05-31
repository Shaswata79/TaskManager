package shaswata.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.exception.DuplicateEntityException;
import shaswata.taskmanager.exception.InvalidInputException;
import shaswata.taskmanager.exception.ResourceNotFoundException;
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

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    AdminRepository adminRepo;

    @Autowired
    ProjectService projectService;


    /////////////////////////////////////////METHODS CALLED BY CONTROLLER/////////////////////////////////////////////

    @Transactional
    public String assignToProjectService(String email, String projectName, UserDetails currentUser) throws Exception {
        String message;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            message = this.assignUserToProject(email, projectName);
        } else {
            if(projectService.validUserOfProject(currentUser.getUsername(), projectName)){
                message = this.assignUserToProject(email, projectName);
            }else {
                throw new AccessDeniedException("User can only access own projects!");
            }
        }
        return message;
    }


    @Transactional
    public String assignToTaskService(String email, Long id, UserDetails currentUser) throws Exception {
        String message;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            message = this.assignUserToTask(email, id);
        } else {
            if(projectService.validUserOfProject(currentUser.getUsername(), id)){
                message = this.assignUserToTask(email, id);
            }else {
                throw new AccessDeniedException("User can only access own projects!");
            }
        }
        return message;
    }



    @Transactional
    public UserDto getUserService(String email, UserDetails currentUser) throws Exception {
        UserDto userDto;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            userDto = this.getUser(email);

        } else {
            if(currentUser.getUsername().equals(email)){
                userDto = this.getUser(email);
            } else{
                throw new AccessDeniedException("You can only view your own account details!");
            }

        }
        return userDto;
    }




    ////////////////////////////////////////////////ACTUAL SERVICES//////////////////////////////////////////////////

    @Transactional
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
        if(email == null || email == "" || newPassword == null || newPassword == ""){
            throw new InvalidInputException("Email or new password cannot be empty!");
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
    public String assignUserToProject(String email, String projectName) throws Exception {
        if(email == null || email == ""){
            throw new InvalidInputException("Email cannot be empty!");
        }
        if(projectName == null || projectName == "") {
            throw new InvalidInputException("Project name cannot be empty!");
        }

        UserAccount user = userRepo.findUserAccountByEmail(email);
        if(user == null){
            throw new Exception("User account with email '" + email + "' not found.");
        }
        Project project = projectRepo.findProjectByName(projectName);
        if(project == null){
            throw new ResourceNotFoundException("Project '" + projectName + "' not found.");
        }

        List<Project> userProjectList = user.getProjects();
        userProjectList.add(project);
        userRepo.save(user);
        return "User " + email + " assigned to project '" + projectName + "'.";

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
        if(email == null || email == ""){
            throw new InvalidInputException("User email cannot be empty!");
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
        if(email == null || email == ""){
            throw new InvalidInputException("User email cannot be empty!");
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
        if(email == null || email == ""){
            throw new InvalidInputException("User email cannot be empty!");
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
        return dto;
    }


}
