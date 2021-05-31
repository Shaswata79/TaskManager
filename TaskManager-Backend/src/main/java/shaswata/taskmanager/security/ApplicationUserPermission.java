package shaswata.taskmanager.security;

public enum ApplicationUserPermission {
    ADMIN_GetAllUsers("admin:getAllUsers"),
    ADMIN_ProjectsByUser("admin:projectsByUser"),
    ADMIN_TasksByUser("admin:tasksByUser"),
    USER_CreateUser("user:create"),
    USER_ChangePassword("user:changePassword"),
    USER_AssignToProject("user:assignToProject"),
    USER_AssignToTask("user:assignToTask"),
    USER_GetUser("user:get"),
    PROJECT_CreateProject("project:create"),
    PROJECT_GetAllProjects("project:getAll"),
    PROJECT_DeleteProject("project:delete"),
    TASK_CreateTask("task:create"),
    TASK_EditTask("task:edit"),
    TASK_GetTask("task:get"),
    TASK_GetAllTasks("task:getAll"),
    TASK_GetAllTasksByProject("task:getAllByProject"),
    TASK_GetExpiredTasks("task:getExpired"),
    TASK_GetTasksByStatus("task:getByStatus");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }
}
