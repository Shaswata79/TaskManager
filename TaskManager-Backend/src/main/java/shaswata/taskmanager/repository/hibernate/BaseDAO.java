package shaswata.taskmanager.repository.hibernate;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Stream;


@Transactional
public class BaseDAO<T> {

    @Autowired
    private SessionFactory sessionFactory;

    private Class<T> entityClass;

    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private Class<T> getModelClass() {
        if (this.entityClass == null) {
            ParameterizedType thisType = (ParameterizedType) getClass().getGenericSuperclass();
            this.entityClass = Stream.of(thisType.getActualTypeArguments())
                                    .findFirst()
                                    .map(type -> (Class<T>) type)
                                    .orElseThrow(() -> new RuntimeException("No classes found for given type"));
        }
        return this.entityClass;
    }

    private String getDomainClassName() {
        return getModelClass().getName();
    }

    //CREATE
    public T create(T t) {
        getSession().save(t);
        return t;
    }

    //READ
    public T findById(Serializable id) {
        return (T) getSession().get(getModelClass(), id);
    }

    //UPDATE
    public T update(T t) {
        getSession().update(t);
        return t;
    }

    //DELETE
    public void delete(T t) {
        getSession().delete(t);
    }

    public T load(Serializable id) {
        return getSession().load(getModelClass(), id);
    }

    public void deleteById(Serializable id) {
        delete(load(id));
    }

    public void deleteAll() {
        getSession()
                .createQuery("DELETE FROM " + getDomainClassName())
                .executeUpdate();
    }

    public List<T> findAll() {
        List<T> list = getSession()
                .createQuery("FROM " + getDomainClassName())
                .list();
        return list;
    }


}
