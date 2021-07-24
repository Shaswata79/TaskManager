package shaswata.taskmanager.repository.hibernate;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shaswata.taskmanager.model.UserAccount;

import javax.persistence.TypedQuery;



@Repository
@Transactional
public class UserDAO extends BaseDAO<UserAccount>{


    public UserAccount findUserAccountByEmail(String email) {
        Session session = super.getSession();
        TypedQuery<UserAccount> query = session.createQuery("SELECT user FROM UserAccount user WHERE user.email = :email", UserAccount.class);
        query.setParameter("email", email);
        UserAccount result = null;
        try{
            result = query.getSingleResult();
        }catch (Exception e){
        }

        return result;
    }



    public void deleteUserAccountByEmail(String email) {
        Session session = super.getSession();
        UserAccount user = findUserAccountByEmail(email);
        session.delete(user);
    }


}
























/*
package shaswata.taskmanager.repository.other;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shaswata.taskmanager.model.UserAccount;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class UserDAO {

    private final EntityManager entityManager;

    @Transactional
    public UserAccount findUserAccountByEmail(String email){
        TypedQuery<UserAccount> q = entityManager.createQuery("SELECT user FROM UserAccount user WHERE user.email = :email", UserAccount.class);
        q.setParameter("email", email);
        UserAccount result = q.getSingleResult();
        entityManager.close();
        return result;
    }

    @Transactional
    public List<UserAccount> findAll(){
        TypedQuery<UserAccount> q = entityManager.createQuery("SELECT user FROM UserAccount user", UserAccount.class);
        List<UserAccount> resultList = q.getResultList();
        entityManager.close();
        return resultList;
    }

    @Transactional
    public void deleteUserAccountByEmail(String email){
        TypedQuery<UserAccount> query = entityManager.createQuery("DELETE FROM UserAccount user WHERE user.email = :email", UserAccount.class);
        query.setParameter("email", email);
        query.executeUpdate();
    }

    @Transactional
    public void deleteAll(){
        TypedQuery<UserAccount> query = entityManager.createQuery("DELETE FROM UserAccount user", UserAccount.class);
        query.executeUpdate();
    }

    @Transactional
    public UserAccount save(UserAccount userAccount){
        entityManager.persist(userAccount);
        return userAccount;
    }


}


 */