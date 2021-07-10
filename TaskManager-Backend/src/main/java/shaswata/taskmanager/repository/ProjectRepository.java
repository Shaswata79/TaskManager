package shaswata.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shaswata.taskmanager.model.Project;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findProjectById(Long id);

    List<Project> findAll();

    void deleteProjectById(Long id);

    void deleteAll();
}
