package shaswata.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;;
import shaswata.taskmanager.model.UserAccount;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, String> {

    UserAccount findUserAccountByEmail(String email);

    List<UserAccount> findAll();

    void deleteUserAccountByEmail(String email);

    void deleteAll();

}
