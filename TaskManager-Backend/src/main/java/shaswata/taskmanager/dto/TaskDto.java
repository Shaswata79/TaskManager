package shaswata.taskmanager.dto;

import com.sun.istack.NotNull;
import shaswata.taskmanager.model.TaskStatus;


import java.sql.Date;



public class TaskDto {

    @NotNull
    private String projectName;

    @NotNull
    private String description;

    @NotNull
    private TaskStatus status;

    private Date dueDate;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
