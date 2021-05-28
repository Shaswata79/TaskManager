package shaswata.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;
import shaswata.taskmanager.model.AdminAccount;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<AdminAccount, String> {

    AdminAccount findAdminAccountByEmail(String email);

    List<AdminAccount> findAll();

    void deleteAdminAccountByEmail(String email);

    void deleteAll();
}
