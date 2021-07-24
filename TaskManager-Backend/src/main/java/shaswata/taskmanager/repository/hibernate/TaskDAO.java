package shaswata.taskmanager.repository.hibernate;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;

import javax.persistence.TypedQuery;
import java.util.List;


@Repository
@Transactional
public class TaskDAO extends BaseDAO<Task>{


    public List<Task> findTaskByProject(Project project){
        Session session = super.getSession();
        TypedQuery<Task> query = session.createQuery("SELECT task FROM Task task WHERE task.project = :project", Task.class);
        query.setParameter("project", project);
        List<Task> result = query.getResultList();
        return result;
    }

    public List<Task> findTaskByStatus(TaskStatus status){
        Session session = super.getSession();
        TypedQuery<Task> query = session.createQuery("SELECT task FROM Task task WHERE task.status = :status", Task.class);
        query.setParameter("status", status);
        List<Task> result = query.getResultList();
        return result;
    }

}
