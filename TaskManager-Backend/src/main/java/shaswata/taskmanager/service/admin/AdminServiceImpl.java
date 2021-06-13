package shaswata.taskmanager.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.exception.InvalidInputException;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.project.ProjectService;
import shaswata.taskmanager.service.task.TaskService;
import shaswata.taskmanager.service.user.UserService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    //Admin exclusive services


    private final UserRepository userRepo;

    @Transactional
    public List<UserDto> getAllUsers(){
        List<UserAccount> users = userRepo.findAll();
        List<UserDto> userDtos = users.stream().map(UserService::userToDTO).collect(Collectors.toList());

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
        List<TaskDto> taskDtoList = userTasks.stream().map(TaskService::taskToDTO).collect(Collectors.toList());

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
        List<ProjectDto> projectDtoList = userProjects.stream().map(ProjectService::projectToDTO).collect(Collectors.toList());

        return projectDtoList;

    }


}
