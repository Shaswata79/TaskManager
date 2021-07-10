package shaswata.taskmanager.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;


@MappedSuperclass
@Getter
@Setter
public abstract class Account {

    private String email;

    private String password;

    private String name;

}
