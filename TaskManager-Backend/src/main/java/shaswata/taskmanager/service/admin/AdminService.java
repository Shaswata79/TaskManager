package shaswata.taskmanager.service.admin;

import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.dto.UserDto;

import java.util.List;


@Service
public interface AdminService {
    //Admin exclusive services

    List<UserDto> getAllUsers();
    List<TaskDto> getTasksByUser(String email) throws Exception;
    List<ProjectDto> getProjectsByUser(String email) throws Exception;


}
