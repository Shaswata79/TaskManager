package shaswata.taskmanager.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static shaswata.taskmanager.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    ADMIN(Sets.newHashSet(ADMIN_GetAllUsers,
            ADMIN_ProjectsByUser,
            ADMIN_TasksByUser,
            USER_CreateUser,
            USER_AssignToProject,
            USER_AssignToTask,
            USER_GetUser,
            PROJECT_CreateProject,
            PROJECT_GetAllProjects,
            PROJECT_DeleteProject,
            TASK_CreateTask,
            TASK_EditTask,
            TASK_GetTask,
            TASK_GetAllTasks,
            TASK_GetAllTasksByProject,
            TASK_GetExpiredTasks,
            TASK_GetTasksByStatus,
            USER_CreateUser)),

    USER(Sets.newHashSet(USER_CreateUser,
            USER_AssignToProject,
            USER_AssignToTask,
            USER_ChangePassword,
            USER_GetUser,
            PROJECT_CreateProject,
            PROJECT_GetAllProjects,
            PROJECT_DeleteProject,
            TASK_CreateTask,
            TASK_EditTask,
            TASK_GetTask,
            TASK_GetAllTasks,
            TASK_GetAllTasksByProject,
            TASK_GetExpiredTasks,
            TASK_GetTasksByStatus));


    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                                                        .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                                                        .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
