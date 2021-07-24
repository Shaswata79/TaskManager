package shaswata.taskmanager.service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.AdminDto;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.exception.DuplicateEntityException;
import shaswata.taskmanager.exception.InvalidInputException;
import shaswata.taskmanager.model.AdminAccount;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.hibernate.AdminDAO;
import shaswata.taskmanager.repository.hibernate.UserDAO;
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


    private final UserDAO userRepo;
    private final AdminDAO adminRepo;
    private final PasswordEncoder passwordEncoder;




    @Transactional
    public List<UserDto> getAllUsers(){
        List<UserAccount> users = userRepo.findAll();
        List<UserDto> userDtos = users.stream()
                                        .map(UserService::userToDTO)
                                        .collect(Collectors.toList());

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
        List<TaskDto> taskDtoList = userTasks.stream()
                                                .map(TaskService::taskToDTO)
                                                .collect(Collectors.toList());

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
        List<ProjectDto> projectDtoList = userProjects.stream()
                                                        .map(ProjectService::projectToDTO)
                                                        .collect(Collectors.toList());

        return projectDtoList;

    }



    @Override
    public AdminDto createAdmin(AdminDto dto) throws Exception {
        if(dto.getName() == null || dto.getName() == ""){
            throw new InvalidInputException("Name cannot be empty!");
        }
        if(dto.getEmail() == null || dto.getEmail() == "" || dto.getPassword() == null || dto.getPassword() == ""){
            throw new InvalidInputException("Email or password cannot be empty!");
        }

        if(adminRepo.findAdminAccountByEmail(dto.getEmail()) != null){
            throw new DuplicateEntityException("Account with email '" + dto.getEmail() + "' already exists.");
        }

        AdminAccount admin = new AdminAccount();
        admin.setName(dto.getName());
        admin.setEmail(dto.getEmail());
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));

        adminRepo.create(admin);
        return AdminService.adminToDTO(admin);
    }


}
