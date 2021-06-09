package shaswata.taskmanager.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import shaswata.taskmanager.model.TaskStatus;

import java.sql.Date;



@Getter
@Setter
public class TaskDto {

    @NotNull
    private String projectName;

    @NotNull
    private String description;

    @NotNull
    private TaskStatus status;

    private Date dueDate;

    private Long id;


}
