package shaswata.taskmanager.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.exception.ResourceNotFoundException;
import shaswata.taskmanager.repository.AdminRepository;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.impl.*;


@Service
@RequiredArgsConstructor
public class ServiceFactory {

    private final UserRepository userRepo;
    private final AdminRepository adminRepo;
    private final TaskRepository taskRepo;
    private final ProjectRepository projectRepo;

    private final TaskServiceAdminImpl taskServiceAdmin;
    private final TaskServiceUserImpl taskServiceUser;
    private final ProjectServiceAdminImpl projectServiceAdmin;
    private final ProjectServiceUserImpl projectServiceUser;
    private final UserServiceAdminImpl userServiceAdmin;
    private final UserServiceUserImpl userServiceUser;


    public TaskService getTaskService(UserDetails currentUser) throws Exception {
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            return taskServiceAdmin;
        } else if(userRepo.findUserAccountByEmail(currentUser.getUsername()) != null){
            return taskServiceUser;
        } else{
            throw new ResourceNotFoundException("No services found for current user type");
        }
    }


    public ProjectService getProjectService(UserDetails currentUser) throws Exception {
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            return projectServiceAdmin;
        } else if(userRepo.findUserAccountByEmail(currentUser.getUsername()) != null){
            return projectServiceUser;
        } else{
            throw new ResourceNotFoundException("No services found for current user type");
        }
    }


    public UserService getUserService(UserDetails currentUser) throws Exception {
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            return userServiceAdmin;
        } else if(userRepo.findUserAccountByEmail(currentUser.getUsername()) != null){
            return userServiceUser;
        } else{
            throw new ResourceNotFoundException("No services found for current user type");
        }
    }


    public UserServiceUserImpl getUserServiceUser(){
        return userServiceUser;
    }



}
