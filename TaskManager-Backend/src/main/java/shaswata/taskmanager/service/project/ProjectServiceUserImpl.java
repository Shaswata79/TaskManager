package shaswata.taskmanager.service.project;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.exception.InvalidInputException;
import shaswata.taskmanager.exception.ResourceNotFoundException;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.hibernate.ProjectDAO;
import shaswata.taskmanager.repository.hibernate.TaskDAO;
import shaswata.taskmanager.repository.hibernate.UserDAO;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProjectServiceUserImpl implements ProjectService {

    private final ProjectDAO projectRepo;
    private final TaskDAO taskRepo;
    private final UserDAO userRepo;


    @Transactional
    @Override
    public ProjectDto createProject(ProjectDto dto, UserDetails currentUser) throws Exception {
        if (dto.getName() == null || dto.getName() == "") {
            throw new InvalidInputException("Project name cannot be empty!");
        }

        String name = dto.getName();
        List<Task> taskList = new ArrayList<Task>();
        Project project = new Project();
        project.setName(name);
        project.setTasks(taskList);
        project = projectRepo.create(project);

        if (dto.getTasks() != null) {
            for (TaskDto taskDto : dto.getTasks()) {
                Task task = new Task();
                task.setDescription(taskDto.getDescription());
                task.setStatus(taskDto.getStatus());
                task.setDueDate(taskDto.getDueDate());
                task.setProject(project);
                taskRepo.create(task);

                taskList.add(task);
            }
            project.setTasks(taskList);
            project = projectRepo.update(project);
        }

        //assign current user to project
        UserAccount user = userRepo.findUserAccountByEmail(currentUser.getUsername());
        List<Project> userProjectList = user.getProjects();
        userProjectList.add(project);
        user.setProjects(userProjectList);
        userRepo.update(user);

        return ProjectService.projectToDTO(project);
    }


    @Transactional
    @Override
    public List<ProjectDto> getAllProjects(UserDetails currentUser) throws Exception {
        UserAccount user = userRepo.findUserAccountByEmail(currentUser.getUsername());
        List<Project> projectList = user.getProjects();

        if (projectList == null) {
            return null;
        }

        List<ProjectDto> projectDtoList = projectList.stream()
                                                        .map(ProjectService::projectToDTO)
                                                        .collect(Collectors.toList());
        return projectDtoList;
    }


    @Transactional
    @Override
    public String deleteProject(Long id, UserDetails currentUser) throws Exception {
        if (id == null) {
            throw new InvalidInputException("Project id cannot be empty!");
        }

        UserAccount user = userRepo.findUserAccountByEmail(currentUser.getUsername());
        List<Project> userProjectList = user.getProjects();
        Project project = projectRepo.findById(id);

        if ((project == null) || (userProjectList == null)) {
            throw new ResourceNotFoundException("Project with id '" + id + "' not found!");
        }

        if (userProjectList.contains(project)) {

            for (UserAccount thisUser : userRepo.findAll()) {
                //remove the project
                if (thisUser.getProjects().contains(project)) {
                    thisUser.getProjects().remove(project);
                    thisUser = userRepo.update(thisUser);

                    //remove the tasks of this project from all users
                    for (Task task : project.getTasks()) {
                        if (thisUser.getTasks().contains(task)) {
                            thisUser.getTasks().remove(task);
                            thisUser = userRepo.update(thisUser);
                        }
                    }
                }
            }
            projectRepo.deleteById(id);

        } else {
            throw new AccessDeniedException("User can only delete own projects");
        }

        return "Project with id '" + id + "' was deleted.";
    }


}
