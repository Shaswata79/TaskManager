

package shaswata.taskmanager.repository.hibernate;


import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shaswata.taskmanager.model.Project;



@Repository
@Transactional
public class ProjectDAO extends BaseDAO<Project>{


}


