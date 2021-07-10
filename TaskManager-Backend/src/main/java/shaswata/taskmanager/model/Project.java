package shaswata.taskmanager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table
public class Project {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(targetEntity = Task.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "project")
    private List<Task> tasks;


}
