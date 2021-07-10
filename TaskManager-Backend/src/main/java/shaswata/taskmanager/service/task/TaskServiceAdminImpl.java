package shaswata.taskmanager.service.task;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.exception.DuplicateEntityException;
import shaswata.taskmanager.exception.InvalidInputException;
import shaswata.taskmanager.exception.ResourceNotFoundException;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TaskServiceAdminImpl implements TaskService {

    private final ProjectRepository projectRepo;
    private final TaskRepository taskRepo;


    @Transactional
    @Override
    public TaskDto createTask(TaskDto taskDto, UserDetails currentUser) throws Exception {

        if(taskDto.getDescription() == "" || taskDto.getStatus() == null || taskDto.getProjectId() == null || taskDto.getDescription() == null){
            throw new InvalidInputException("Task description, status or project name cannot be empty!");
        }

        Task task = new Task();
        Project project = projectRepo.findProjectById(taskDto.getProjectId());
        if(project == null){
            throw new ResourceNotFoundException("A task can only be created in an existing project!");
        }
        if(project.getTasks().stream().map(Task::getDescription).collect(Collectors.toList()).contains(taskDto.getDescription())){
            throw new DuplicateEntityException("Task already exists in project");
        }

        task.setProject(project);
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setDueDate(taskDto.getDueDate());
        task = taskRepo.save(task);

        List<Task> taskList = project.getTasks();
        taskList.add(task);
        project.setTasks(taskList);
        projectRepo.save(project);

        TaskDto returnDto = TaskService.taskToDTO(task);
        return returnDto;

    }


    @Transactional
    @Override
    public TaskDto editTask(Long id, TaskDto taskDto, UserDetails currentUser) throws Exception {
        if(id == null){
            throw new InvalidInputException("Task ID cannot be empty!");
        }
        Task task = taskRepo.findTaskById(id);
        if(task == null){
            throw new ResourceNotFoundException("Task with given ID not found");
        }
        if(task.getStatus().equals(TaskStatus.closed)){
            throw new Exception("Task has already been closed so it cannot be updated");
        }

        //Note : Task's id and project cannot be changed
        if(taskDto.getDescription() != null && taskDto.getDescription() != ""){
            task.setDescription(taskDto.getDescription());
        }
        if(taskDto.getStatus() != null){
            task.setStatus(taskDto.getStatus());
        }
        if(taskDto.getDueDate() != null){
            task.setDueDate(taskDto.getDueDate());
        }

        task = taskRepo.save(task);
        return TaskService.taskToDTO(task);
    }



    @Transactional
    @Override
    public TaskDto getTask(Long id, UserDetails currentUser) throws Exception {
        if(id == null){
            throw new InvalidInputException("Task ID cannot be empty!");
        }

        Task task = taskRepo.findTaskById(id);
        if(task == null){
            throw new ResourceNotFoundException("Task with given ID not found");
        }

        return TaskService.taskToDTO(task);
    }


    @Transactional
    @Override
    public List<TaskDto> getAllTasks(UserDetails currentUser){
        List<Task> taskList = taskRepo.findAll();
        List<TaskDto> taskDtoList = taskList.stream()
                                                .map(TaskService::taskToDTO)
                                                .collect(Collectors.toList());

        return taskDtoList;
    }




    @Transactional
    @Override
    public List<TaskDto> getTasksByProject(Long projectId, UserDetails currentUser) throws Exception {
        if(projectId == null){
            throw new InvalidInputException("Project name cannot be empty!");
        }
        Project project = projectRepo.findProjectById(projectId);
        if(project == null){
            throw new ResourceNotFoundException("Project with id '" + projectId + "' not found!");
        }

        List<Task> taskList = taskRepo.findTaskByProject(project);
        List<TaskDto> taskDtoList = taskList.stream()
                                                .map(TaskService::taskToDTO)
                                                .collect(Collectors.toList());
        return taskDtoList;
    }


    @Transactional
    @Override
    public List<TaskDto> getTasksByStatus(String status, UserDetails currentUser) throws Exception {
        if(status == null || status == ""){
            throw new InvalidInputException("Task status cannot be empty!");
        }

        TaskStatus taskStatus;
        switch (status){
            case "open":
                taskStatus = TaskStatus.open;
                break;
            case "inProgress":
                taskStatus = TaskStatus.inProgress;
                break;
            case "closed":
                taskStatus = TaskStatus.closed;
                break;
            default:
                throw new InvalidInputException("Not a valid task status");
        }

        List<Task> taskList = taskRepo.findTaskByStatus(taskStatus);
        List<TaskDto> taskDtoList = taskList.stream()
                                                .map(TaskService::taskToDTO)
                                                .collect(Collectors.toList());
        return taskDtoList;
    }




    @Transactional
    @Override
    public List<TaskDto> getExpiredTasks(UserDetails currentUser){
        long millis = System.currentTimeMillis();
        Date currentDate = new Date(millis);

        List<Task> taskList = taskRepo.findAll();
        List<TaskDto> expiredTasks = taskList.stream()
                                                .filter(task -> (task.getDueDate() != null && task.getDueDate().before(currentDate)))
                                                .map(TaskService::taskToDTO)
                                                .collect(Collectors.toList());

        return expiredTasks;
    }







}
