package shaswata.taskmanager.service;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import shaswata.taskmanager.common.ApplicationContextHolder;
import shaswata.taskmanager.exception.ResourceNotFoundException;
import shaswata.taskmanager.common.ApplicationUserRole;
import shaswata.taskmanager.service.admin.AdminServiceImpl;
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



    public TaskService getTaskService(UserDetails currentUser) throws Exception {
        if(hasRole(currentUser, ApplicationUserRole.ADMIN)){
            return ApplicationContextHolder.getContext().getBean(TaskServiceAdminImpl.class);
        }
        if(hasRole(currentUser, ApplicationUserRole.USER)){
            return ApplicationContextHolder.getContext().getBean(TaskServiceUserImpl.class);
        }

        throw new ResourceNotFoundException("No services found for current user type");
    }


    public ProjectService getProjectService(UserDetails currentUser) throws Exception {
        if(hasRole(currentUser, ApplicationUserRole.ADMIN)){
            return ApplicationContextHolder.getContext().getBean(ProjectServiceAdminImpl.class);
        }
        if(hasRole(currentUser, ApplicationUserRole.USER)){
            return ApplicationContextHolder.getContext().getBean(ProjectServiceUserImpl.class);
        }

        throw new ResourceNotFoundException("No services found for current user type");
    }


    public UserService getUserService(UserDetails currentUser) throws Exception {
        if(hasRole(currentUser, ApplicationUserRole.ADMIN)){
            return ApplicationContextHolder.getContext().getBean(UserServiceAdminImpl.class);
        }
        if(hasRole(currentUser, ApplicationUserRole.USER)){
            return ApplicationContextHolder.getContext().getBean(UserServiceUserImpl.class);
        }

        throw new ResourceNotFoundException("No services found for current user type");
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


    private boolean hasRole(UserDetails userDetails, ApplicationUserRole applicationUserRole){
        if (userDetails != null && userDetails.getAuthorities()
                                            .stream()
                                            .anyMatch(role -> role.getAuthority()
                                            .equals("ROLE_" + applicationUserRole.name())))
        {
            return true;
        }

        return false;

    }

}
