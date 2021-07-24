
/*
package shaswata.taskmanager.repository.springdatajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAll();

    Task findTaskById(Long id);

    List<Task> findTaskByProject(Project project);

    List<Task> findTaskByStatus(TaskStatus status);

    void deleteById(Long id);

    void deleteAll();
}


 */