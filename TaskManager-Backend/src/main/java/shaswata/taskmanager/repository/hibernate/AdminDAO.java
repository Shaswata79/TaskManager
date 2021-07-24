package shaswata.taskmanager.repository.hibernate;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shaswata.taskmanager.model.AdminAccount;

import javax.persistence.TypedQuery;


@Repository
@Transactional
public class AdminDAO extends BaseDAO<AdminAccount>{

    public AdminAccount findAdminAccountByEmail(String email){
        Session session = super.getSession();
        TypedQuery<AdminAccount> query = session.createQuery("SELECT admin FROM AdminAccount admin WHERE admin.email = :email", AdminAccount.class);
        query.setParameter("email", email);
        AdminAccount result = null;
        try{
             result = query.getSingleResult();
        } catch (Exception e) {
        }

        return result;
    }


    public void deleteAdminAccountByEmail(String email){
        Session session = super.getSession();
        AdminAccount admin = findAdminAccountByEmail(email);
        session.delete(admin);
    }

}
