package shaswata.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shaswata.taskmanager.model.AdminAccount;
import shaswata.taskmanager.model.UserAccount;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, String> {

    UserAccount findUserAccountByEmail(String email);

    //UserAccount findUserAccountByToken(String token);

    List<UserAccount> findAll();

    void deleteUserAccountByEmail(String email);

    void deleteAll();

}
