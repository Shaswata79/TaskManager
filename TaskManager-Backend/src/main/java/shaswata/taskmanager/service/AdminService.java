package shaswata.taskmanager.service;

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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminService {
    //Admin exclusive services

    private final UserRepository userRepo;


    @Transactional
    public List<UserDto> getAllUsers(){
        List<UserAccount> users = userRepo.findAll();
        List<UserDto> userDtos = new ArrayList<>();

        for(UserAccount user : users){
            userDtos.add(UserService.userToDTO(user));
        }

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
        List<TaskDto> taskDtoList = new ArrayList<>();

        for(Task task : userTasks){
            taskDtoList.add(TaskService.taskToDTO(task));
        }

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
        List<ProjectDto> projectDtoList = new ArrayList<>();

        for(Project project : userProjects){
            projectDtoList.add(ProjectService.projectToDTO(project));
        }

        return projectDtoList;

    }
}
