package shaswata.taskmanager.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.model.Task;

import java.util.List;


@Service
public interface TaskService {

    TaskDto createTask(TaskDto taskDto, UserDetails currentUser) throws Exception;
    TaskDto editTask(Long id, TaskDto taskDto, UserDetails currentUser) throws Exception;
    TaskDto getTask(Long id, UserDetails currentUser) throws Exception;
    List<TaskDto> getAllTasks(UserDetails currentUser);
    List<TaskDto> getTasksByProject(String projectName, UserDetails currentUser) throws Exception;
    List<TaskDto> getTasksByStatus(String status, UserDetails currentUser) throws Exception;
    List<TaskDto> getExpiredTasks(UserDetails currentUser);



    static TaskDto taskToDTO(Task task){
        TaskDto taskDto = new TaskDto();
        taskDto.setDescription(task.getDescription());
        taskDto.setDueDate(task.getDueDate());
        taskDto.setStatus(task.getStatus());
        taskDto.setProjectName(task.getProject().getName());
        taskDto.setId(task.getId());
        return taskDto;
    }
}
