package shaswata.taskmanager.service.admin;

import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.AdminDto;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.model.AdminAccount;
import shaswata.taskmanager.model.UserAccount;

import java.util.List;


@Service
public interface AdminService {
    //Admin exclusive services

    List<UserDto> getAllUsers();
    List<TaskDto> getTasksByUser(String email) throws Exception;
    List<ProjectDto> getProjectsByUser(String email) throws Exception;
    AdminDto createAdmin(AdminDto admin) throws Exception;


    static AdminDto adminToDTO(AdminAccount user) {
        AdminDto dto = new AdminDto();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPassword(user.getPassword());
        return dto;
    }


}
