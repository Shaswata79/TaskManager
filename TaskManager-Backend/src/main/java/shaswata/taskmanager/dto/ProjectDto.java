package shaswata.taskmanager.dto;

import com.sun.istack.NotNull;

import java.util.List;

public class ProjectDto {

    @NotNull
    private String name;

    private List<TaskDto> tasks;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }
}
