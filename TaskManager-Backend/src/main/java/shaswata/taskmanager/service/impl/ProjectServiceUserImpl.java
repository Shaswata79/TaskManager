package shaswata.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
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
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.ProjectService;

import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
public class ProjectServiceUserImpl implements ProjectService {

    private final ProjectRepository projectRepo;
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;


    @Override
    public ProjectDto createProject(ProjectDto dto, UserDetails currentUser) throws Exception {
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

        //assign current user to project
        UserAccount user = userRepo.findUserAccountByEmail(currentUser.getUsername());
        List<Project> userProjectList = user.getProjects();
        userProjectList.add(project);
        user.setProjects(userProjectList);
        userRepo.save(user);

        return ProjectService.projectToDTO(project);
    }


    @Override
    public List<ProjectDto> getAllProjects(UserDetails currentUser) throws Exception {
        UserAccount user = userRepo.findUserAccountByEmail(currentUser.getUsername());
        List<Project> projectList = user.getProjects();
        List<ProjectDto> projectDtoList = new ArrayList<>();

        if(projectDtoList == null){
            return null;
        }

        for(Project project : projectList){
            projectDtoList.add(ProjectService.projectToDTO(project));
        }

        return projectDtoList;
    }



    @Override
    public String deleteProject(String name, UserDetails currentUser) throws Exception {
        if(name == null || name == ""){
            throw new InvalidInputException("Project name cannot be empty!");
        }

        UserAccount user = userRepo.findUserAccountByEmail(currentUser.getUsername());
        List<Project> userProjectList = user.getProjects();
        Project project = projectRepo.findProjectByName(name);

        if((project == null) || (userProjectList == null)){
            throw new ResourceNotFoundException("Project '" + name + "' not found!");
        }

        if(userProjectList.contains(project)){

            for(UserAccount thisUser : userRepo.findAll()){
                if(user.getProjects().contains(project)){
                    thisUser.getProjects().remove(project);
                    userRepo.save(thisUser);
                }
            }
            projectRepo.deleteProjectByName(name);

        } else{
            throw new AccessDeniedException("User can only delete own projects");
        }

        return "Project '" + name + "' was deleted.";
    }
}
