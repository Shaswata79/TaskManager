package shaswata.taskmanager.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ProjectDto {

    @NotNull
    private String name;

    private List<TaskDto> tasks;

}
