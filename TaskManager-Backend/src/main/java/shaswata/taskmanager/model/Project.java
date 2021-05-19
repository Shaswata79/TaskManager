package shaswata.taskmanager.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Project {

    private String name;

    @Id
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    ///////////////////////////////////////////////////////////

    private List<Task> tasks;

    @OneToMany(targetEntity = Task.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "project")
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

}
