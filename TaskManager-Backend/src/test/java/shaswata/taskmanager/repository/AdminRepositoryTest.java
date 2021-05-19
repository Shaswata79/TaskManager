package shaswata.taskmanager.repository;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import shaswata.taskmanager.model.AdminAccount;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class AdminRepositoryTest {

    @Autowired
    AdminRepository adminRepo;


    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        adminRepo.deleteAll();
    }


    @Test
    public void testPersistAndLoadAdmin() {

        //create admin
        String name = "New Admin";
        String email = "newAdmin@gmail.com";
        String password = "password123";

        AdminAccount admin = new AdminAccount();
        admin.setEmail(email);
        admin.setName(name);
        admin.setPassword(password);

        //save to database
        adminRepo.save(admin);


        admin = null;


        // load from database
        admin = adminRepo.findAdminAccountByEmail(email);
        assertNotNull(admin);
        assertEquals(email, admin.getEmail());
        assertEquals(name, admin.getName());
        assertEquals(password, admin.getPassword());


    }

    @Test
    void testDeleteAdmin() {

        // create administrator
        String name = "New Admin";
        String email = "newAdmin@gmail.com";
        String password = "password123";

        AdminAccount admin = new AdminAccount();
        admin.setEmail(email);
        admin.setName(name);
        admin.setPassword(password);

        // save admin in database
        adminRepo.save(admin);

        admin = null;
        admin = adminRepo.findAdminAccountByEmail(email);
        assertNotNull(admin);


        // delete from database
        admin = null;
        adminRepo.deleteAdminAccountByEmail(email);

        assertNull(adminRepo.findAdminAccountByEmail(email));

    }



}
