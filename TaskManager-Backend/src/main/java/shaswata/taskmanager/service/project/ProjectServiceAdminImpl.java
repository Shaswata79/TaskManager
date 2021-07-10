package shaswata.taskmanager.service.project;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.exception.InvalidInputException;
import shaswata.taskmanager.exception.ResourceNotFoundException;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProjectServiceAdminImpl implements ProjectService {

    private final ProjectRepository projectRepo;
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;


    @Transactional
    @Override
    public ProjectDto createProject(ProjectDto dto, UserDetails currentUser) throws Exception {
        if(dto.getName() == null || dto.getName() == ""){
            throw new InvalidInputException("Project name cannot be empty!");
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

        return ProjectService.projectToDTO(project);
    }


    @Transactional
    @Override
    public List<ProjectDto> getAllProjects(UserDetails currentUser) throws Exception {
        List<Project> projectList = projectRepo.findAll();
        List<ProjectDto> projectDtoList = projectList.stream()
                                                        .map(ProjectService::projectToDTO)
                                                        .collect(Collectors.toList());

        return projectDtoList;
    }



    @Transactional
    @Override
    public String deleteProject(Long id, UserDetails currentUser) throws Exception {
        if(id == null){
            throw new InvalidInputException("Project id cannot be empty!");
        }
        Project project = projectRepo.findProjectById(id);
        if(project == null){
            throw new ResourceNotFoundException("Project with id'" + id + "' not found!");
        }

        //first remove the tasks from users
        project.getTasks().forEach(this::deleteUserTasks);

        //then remove the project from users
        deleteUserProjects(project);

        //finally delete the project from repo
        projectRepo.deleteProjectById(id);
        return "Project with id '" + id + "' was deleted.";
    }


    private void deleteUserTasks(Task task){
        for(UserAccount user : userRepo.findAll()){
            List<Task> userTaskList = user.getTasks();
            userTaskList.remove(task);
            user.setTasks(userTaskList);
            userRepo.save(user);
        }
    }

    private void deleteUserProjects(Project project){
        for(UserAccount user : userRepo.findAll()){
            List<Project> userProjectList = user.getProjects();
            userProjectList.remove(project);
            user.setProjects(userProjectList);
            userRepo.save(user);
        }
    }



}
