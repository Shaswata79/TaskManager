

package shaswata.taskmanager.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AdminAccount extends Account{

    @Id
    public String getEmail() {
            return super.getEmail();
    }

    public void setEmail(String email) {
        super.setEmail(email);
    }

}


