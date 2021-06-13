package shaswata.taskmanager.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.model.UserAccount;

@Service
public interface UserService {


    String assignUserToProject(String email, String projectName, UserDetails currentUser) throws Exception;
    String assignUserToTask(String email, Long id, UserDetails currentUser) throws Exception;
    UserDto getUser(String email, UserDetails currentUser) throws Exception;
    UserDto createUser(UserDto dto) throws Exception;



    static UserDto userToDTO(UserAccount user) {
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPassword(user.getPassword());
        return dto;
    }


}
