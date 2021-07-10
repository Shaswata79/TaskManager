package shaswata.taskmanager.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"email"}))
public class UserAccount extends Account{

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(targetEntity = Project.class, fetch = FetchType.LAZY)
    private List<Project> projects;

    @ManyToMany(targetEntity = Task.class, fetch = FetchType.LAZY)
    private List<Task> tasks;


}
