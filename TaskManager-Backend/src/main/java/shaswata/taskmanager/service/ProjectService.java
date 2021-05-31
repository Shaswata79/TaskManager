package shaswata.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
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
public class ProjectService {

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    TaskRepository taskRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    AdminRepository adminRepo;

    @Autowired
    UserService userService;


    //////////////////////////////////////METHODS CALLED BY CONTROLLER////////////////////////////////////////////

    @Transactional
    public ProjectDto createProjectService(ProjectDto dto, UserDetails currentUser) throws Exception {
        ProjectDto projectDto;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            projectDto = this.createProject(dto);
        } else{
            projectDto = this.createProject(dto);
            userService.assignUserToProject(currentUser.getUsername(), projectDto.getName());
        }
        return projectDto;
    }


    @Transactional
    public List<ProjectDto> getAllProjectService(UserDetails currentUser){
        List<ProjectDto> projectDtoList;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            projectDtoList = this.getAllProjects();
        } else{
            UserAccount user = userRepo.findUserAccountByEmail(currentUser.getUsername());
            projectDtoList = this.getAllProjects(user);
        }
        return projectDtoList;
    }



    @Transactional
    public String deleteProjectService(String name, UserDetails currentUser) throws Exception {
        String message;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            message = this.deleteProject(name);
        } else{
            UserAccount user = userRepo.findUserAccountByEmail(currentUser.getUsername());
            message = this.deleteProject(name, user);
        }
        return message;
    }



    //////////////////////////////////////ACTUAL SERVICES/////////////////////////////////////////////////////////


    @Transactional
    public ProjectDto createProject(ProjectDto dto) throws Exception {
        if(dto.getName() == null || dto.getName() == ""){
            throw new InvalidInputException("Project name cannot be empty!");
        }
        if(projectRepo.findProjectByName(dto.getName()) != null){
            throw new DuplicateEntityException("Project '" + dto.getName() + "' already exists!");
        }

        String name = dto.getName();
        List<Task> taskList = new ArrayList<Task>();
        Project project = new Project();
        project.setName(name);
        project.setTasks(taskList);
        project = projectRepo.save(project);

        if(dto.getTasks() != null){
            for(TaskDto taskDto : dto.getTasks()){
                Task task = new Task();
                task.setDescription(taskDto.getDescription());
                task.setStatus(taskDto.getStatus());
                task.setDueDate(taskDto.getDueDate());
                task.setProject(project);
                taskRepo.save(task);

                taskList.add(task);
            }
            project.setTasks(taskList);
            project = projectRepo.save(project);
        }

        return projectToDTO(project);
    }


    @Transactional
    public List<ProjectDto> getAllProjects(){
        List<Project> projectList = projectRepo.findAll();
        List<ProjectDto> projectDtoList = new ArrayList<>();

        for(Project project : projectList){
            projectDtoList.add(projectToDTO(project));
        }

        return projectDtoList;
    }


    @Transactional
    public List<ProjectDto> getAllProjects(UserAccount user){
        List<Project> projectList = user.getProjects();
        List<ProjectDto> projectDtoList = new ArrayList<>();

        if(projectDtoList == null){
            return null;
        }

        for(Project project : projectList){
            projectDtoList.add(projectToDTO(project));
        }

        return projectDtoList;
    }


    @Transactional
    public String deleteProject(String name) throws Exception {
        if(name == null || name == ""){
            throw new InvalidInputException("Project name cannot be empty!");
        }
        Project project = projectRepo.findProjectByName(name);
        if(project == null){
            throw new ResourceNotFoundException("Project '" + name + "' not found!");
        }

        for(UserAccount user : userRepo.findAll()){
            if(user.getProjects().contains(project)){
                user.getProjects().remove(project);
                userRepo.save(user);
            }
        }
        projectRepo.deleteProjectByName(name);
        return "Project '" + name + "' was deleted.";
    }



    @Transactional
    public String deleteProject(String name, UserAccount user) throws Exception {
        List<Project> userProjectList = user.getProjects();
        Project project = projectRepo.findProjectByName(name);

        if((project != null) && (userProjectList != null)){
            if(userProjectList.contains(project)){
                String message = deleteProject(name);
                return message;
            } else{
                throw new AccessDeniedException("User can only delete own projects");
            }

        } else {
            throw new ResourceNotFoundException("No such project found!");
        }
    }



    public static ProjectDto projectToDTO(Project project){
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(project.getName());
        List<TaskDto> taskDtoList = new ArrayList<>();

        for(Task task : project.getTasks()){
            taskDtoList.add(TaskService.taskToDTO(task));
        }
        projectDto.setTasks(taskDtoList);

        return projectDto;
    }


    public boolean validUserOfProject(String username, String projectName){
        UserAccount user = userRepo.findUserAccountByEmail(username);
        List<Project> userProjectList = user.getProjects();
        Project project = projectRepo.findProjectByName(projectName);
        if(project != null && userProjectList != null){
            if(userProjectList.contains(project)){
                return true;
            }
        }
        return false;
    }


    public boolean validUserOfProject(String username, Long id){
        Task task = taskRepo.findTaskById(id);
        String projectName = task.getProject().getName();
        UserAccount user = userRepo.findUserAccountByEmail(username);

        List<Project> userProjectList = user.getProjects();
        Project project = projectRepo.findProjectByName(projectName);
        if(project != null && userProjectList != null){
            if(userProjectList.contains(project)){
                return true;
            }
        }
        return false;
    }


}
