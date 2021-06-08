package shaswata.taskmanager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.exception.ResourceNotFoundException;
import shaswata.taskmanager.repository.AdminRepository;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.impl.*;


@Service
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


    @Autowired
    public ServiceFactory(UserRepository userRepo, AdminRepository adminRepo, TaskRepository taskRepo, ProjectRepository projectRepo, TaskServiceAdminImpl taskServiceAdmin, TaskServiceUserImpl taskServiceUser, ProjectServiceAdminImpl projectServiceAdmin, ProjectServiceUserImpl projectServiceUser, UserServiceAdminImpl userServiceAdmin, UserServiceUserImpl userServiceUser) {
        this.userRepo = userRepo;
        this.adminRepo = adminRepo;
        this.taskRepo = taskRepo;
        this.projectRepo = projectRepo;

        this.taskServiceAdmin = taskServiceAdmin;
        this.taskServiceUser = taskServiceUser;
        this.projectServiceAdmin = projectServiceAdmin;
        this.projectServiceUser = projectServiceUser;
        this.userServiceAdmin = userServiceAdmin;
        this.userServiceUser = userServiceUser;
    }



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
