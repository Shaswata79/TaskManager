

package shaswata.taskmanager.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "Admin_Account", uniqueConstraints = @UniqueConstraint(columnNames={"email"}))
public class AdminAccount extends Account{

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

}


