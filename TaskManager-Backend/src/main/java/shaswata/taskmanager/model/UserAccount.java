package shaswata.taskmanager.model;

import javax.persistence.*;
import java.util.List;


@Entity
public class UserAccount extends Account{

    @Id
    public String getEmail() {
        return super.getEmail();
    }
    public void setEmail(String email) {
        super.setEmail(email);
    }

    ///////////////////////////////////////////////

    private List<Project> projects;

    @ManyToMany(targetEntity = Project.class, fetch = FetchType.LAZY)
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    //////////////////////////////////////////////////////

    private List<Task> tasks;

    @ManyToMany(targetEntity = Task.class, fetch = FetchType.LAZY)
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


}
