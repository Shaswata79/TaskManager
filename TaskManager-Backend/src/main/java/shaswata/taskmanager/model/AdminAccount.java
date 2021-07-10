

package shaswata.taskmanager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames={"email"}))
public class AdminAccount extends Account{

    @Id
    @GeneratedValue
    private Long id;

}


