package shaswata.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    TaskRepository taskRepo;

    @Autowired
    UserRepository userRepo;



    @Transactional
    public ProjectDto createProject(ProjectDto dto) throws Exception {
        if(dto.getName() == null){
            throw new Exception("Project name cannot be empty!");
        }
        if(projectRepo.findProjectByName(dto.getName()) != null){
            throw new Exception("Project '" + dto.getName() + "' already exists!");
        }

        String name = dto.getName();
        List<Task> taskList = new ArrayList<Task>();
        Project project = new Project();
        project.setName(name);
        project.setTasks(taskList);
        project = projectRepo.save(project);

        if(dto.getTasks() != null){
            for(TaskDto taskDto : dto.getTasks()){
                Task task = new Task();
                task.setDescription(taskDto.getDescription());
                task.setStatus(taskDto.getStatus());
                task.setDueDate(taskDto.getDueDate());
                task.setProject(project);
                taskRepo.save(task);

                taskList.add(task);
            }
            project.setTasks(taskList);
            project = projectRepo.save(project);
        }

        return projectToDTO(project);
    }


    @Transactional
    public List<ProjectDto> getAllProjects(){
        List<Project> projectList = projectRepo.findAll();
        List<ProjectDto> projectDtoList = new ArrayList<>();

        for(Project project : projectList){
            projectDtoList.add(projectToDTO(project));
        }

        return projectDtoList;
    }


    @Transactional
    public String deleteProject(String name) throws Exception {
        if(name == null){
            throw new Exception("Project name cannot be empty!");
        }
        Project project = projectRepo.findProjectByName(name);
        if(project == null){
            throw new Exception("Project '" + name + "' not found!");
        }

        for(UserAccount user : userRepo.findAll()){
            if(user.getProjects().contains(project)){
                user.getProjects().remove(project);
                userRepo.save(user);
            }
        }
        projectRepo.deleteProjectByName(name);
        return "Project '" + name + "' was deleted.";
    }



    public static ProjectDto projectToDTO(Project project){
        ProjectDto projectDto = new ProjectDto();
        projectDto.setName(project.getName());
        List<TaskDto> taskDtoList = new ArrayList<>();

        for(Task task : project.getTasks()){
            taskDtoList.add(TaskService.taskToDTO(task));
        }
        projectDto.setTasks(taskDtoList);

        return projectDto;
    }


}
