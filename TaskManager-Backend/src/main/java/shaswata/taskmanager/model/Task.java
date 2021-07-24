package shaswata.taskmanager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table
public class Task {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    private Project project;

    private String description;

    private TaskStatus status;

    private Date dueDate;

    @Version
    private Long version;

}
