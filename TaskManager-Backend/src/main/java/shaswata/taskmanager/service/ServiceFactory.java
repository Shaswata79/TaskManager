package shaswata.taskmanager.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import shaswata.taskmanager.ApplicationContextHolder;
import shaswata.taskmanager.dto.AuthenticationResponse;
import shaswata.taskmanager.exception.ResourceNotFoundException;
import shaswata.taskmanager.repository.AdminRepository;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.admin.AdminService;
import shaswata.taskmanager.service.admin.AdminServiceImpl;
import shaswata.taskmanager.service.authentication.AuthenticationService;
import shaswata.taskmanager.service.authentication.AuthenticationServiceImpl;
import shaswata.taskmanager.service.project.ProjectService;
import shaswata.taskmanager.service.project.ProjectServiceAdminImpl;
import shaswata.taskmanager.service.project.ProjectServiceUserImpl;
import shaswata.taskmanager.service.task.TaskService;
import shaswata.taskmanager.service.task.TaskServiceAdminImpl;
import shaswata.taskmanager.service.task.TaskServiceUserImpl;
import shaswata.taskmanager.service.user.UserService;
import shaswata.taskmanager.service.user.UserServiceAdminImpl;
import shaswata.taskmanager.service.user.UserServiceUserImpl;


@Component
@RequiredArgsConstructor
public class ServiceFactory {


    private final UserRepository userRepo;
    private final AdminRepository adminRepo;



    public TaskService getTaskService(UserDetails currentUser) throws Exception {
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            return ApplicationContextHolder.getContext().getBean(TaskServiceAdminImpl.class);
        } else if(userRepo.findUserAccountByEmail(currentUser.getUsername()) != null){
            return ApplicationContextHolder.getContext().getBean(TaskServiceUserImpl.class);
        } else{
            throw new ResourceNotFoundException("No services found for current user type");
        }
    }


    public ProjectService getProjectService(UserDetails currentUser) throws Exception {
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            return ApplicationContextHolder.getContext().getBean(ProjectServiceAdminImpl.class);
        } else if(userRepo.findUserAccountByEmail(currentUser.getUsername()) != null){
            return ApplicationContextHolder.getContext().getBean(ProjectServiceUserImpl.class);
        } else{
            throw new ResourceNotFoundException("No services found for current user type");
        }
    }


    public UserService getUserService(UserDetails currentUser) throws Exception {
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            return ApplicationContextHolder.getContext().getBean(UserServiceAdminImpl.class);
        } else if(userRepo.findUserAccountByEmail(currentUser.getUsername()) != null){
            return ApplicationContextHolder.getContext().getBean(UserServiceUserImpl.class);
        } else{
            throw new ResourceNotFoundException("No services found for current user type");
        }
    }


    public UserServiceUserImpl getUserServiceUser(){
        return ApplicationContextHolder.getContext().getBean(UserServiceUserImpl.class);
    }

    public AdminServiceImpl getAdminService(){
        return ApplicationContextHolder.getContext().getBean(AdminServiceImpl.class);
    }

    public AuthenticationServiceImpl getAuthenticationService(){
        return ApplicationContextHolder.getContext().getBean(AuthenticationServiceImpl.class);
    }

}
