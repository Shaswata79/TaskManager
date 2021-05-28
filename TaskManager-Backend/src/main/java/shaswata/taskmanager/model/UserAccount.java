package shaswata.taskmanager.model;



import javax.persistence.*;

import java.util.List;


@Entity
public class UserAccount extends Account{

    private List<Project> projects;

    private List<Task> tasks;

    public UserAccount() {
        this.setRole("USER");
    }


    ///////////////////////////////////////////////


    @ManyToMany(targetEntity = Project.class, fetch = FetchType.LAZY)
    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }


    //////////////////////////////////////////////////////


    @ManyToMany(targetEntity = Task.class, fetch = FetchType.LAZY)
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;}


    ////////////////////////////////////////////////////////////


    @Id
    public String getEmail() {
        return super.getEmail();
    }

    public void setEmail(String email) {
        super.setEmail(email);
    }



}
