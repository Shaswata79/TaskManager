package shaswata.taskmanager.service.project;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.service.task.TaskService;

import java.util.ArrayList;
import java.util.List;



@Service
public interface ProjectService {


    ProjectDto createProject(ProjectDto dto, UserDetails currentUser) throws Exception;
    List<ProjectDto> getAllProjects(UserDetails currentUser) throws Exception;
    String deleteProject(Long id, UserDetails currentUser) throws Exception;




    static ProjectDto projectToDTO(Project project){
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(project.getName());
        projectDto.setProjectId(project.getId());
        List<TaskDto> taskDtoList = new ArrayList<>();

        for(Task task : project.getTasks()){
            taskDtoList.add(TaskService.taskToDTO(task));
        }
        projectDto.setTasks(taskDtoList);

        return projectDto;
    }

}
