package shaswata.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shaswata.taskmanager.model.Project;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    Project findProjectByName(String name);

    List<Project> findAll();

    void deleteProjectByName(String name);

    void deleteAll();
}
