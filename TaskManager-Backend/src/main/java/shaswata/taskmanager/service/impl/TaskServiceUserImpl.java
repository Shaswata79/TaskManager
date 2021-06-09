package shaswata.taskmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.exception.DuplicateEntityException;
import shaswata.taskmanager.exception.InvalidInputException;
import shaswata.taskmanager.exception.ResourceNotFoundException;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.TaskService;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TaskServiceUserImpl implements TaskService {

    private final ProjectRepository projectRepo;
    private final TaskRepository taskRepo;
    private final UserRepository userRepo;


    @Transactional
    @Override
    public TaskDto createTask(TaskDto taskDto, UserDetails currentUser) throws Exception {

        if(taskDto.getDescription() == "" || taskDto.getStatus() == null || taskDto.getProjectName() == "" || taskDto.getDescription() == null || taskDto.getProjectName() == null){
            throw new InvalidInputException("Task description, status or project name cannot be empty!");
        }

        Task task = new Task();
        Project project = projectRepo.findProjectByName(taskDto.getProjectName());
        if(project == null){
            throw new ResourceNotFoundException("A task can only be created in an existing project!");
        }

        if(!validUserOfProject(currentUser.getUsername(), taskDto.getProjectName())){
            throw new AccessDeniedException("Not a valid user of this project");
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

        if(!validUserOfTask(currentUser.getUsername(), id)){
            throw new AccessDeniedException("Not a valid user of this task");
        }
        if(task.getStatus().equals(TaskStatus.closed)){
            throw new Exception("Task has already been closed so it cannot be updated");
        }

        //Note : Task's project cannot be changed
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setDueDate(taskDto.getDueDate());
        task = taskRepo.save(task);

        return TaskService.taskToDTO(task);
    }



    @Transactional
    @Override
    public TaskDto getTask(Long id, UserDetails currentUser) throws Exception {
        if(id == null){
            throw new InvalidInputException("Task ID cannot be empty!");
        }

        if(!validUserOfTask(currentUser.getUsername(), id)){
            throw new AccessDeniedException("Not a valid user of this task");
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
        UserAccount user = userRepo.findUserAccountByEmail(currentUser.getUsername());
        List<Task> taskList = user.getTasks();
        List<TaskDto> taskDtoList = new ArrayList<>();

        for (Task task : taskList){
            taskDtoList.add(TaskService.taskToDTO(task));
        }

        return taskDtoList;
    }


    @Transactional
    @Override
    public List<TaskDto> getTasksByProject(String name, UserDetails currentUser) throws Exception {

        if(name == null || name == ""){
            throw new InvalidInputException("Project name cannot be empty!");
        }
        if(!validUserOfProject(currentUser.getUsername(), name)){
            throw new AccessDeniedException("Not a valid user of this project");
        }

        Project project = projectRepo.findProjectByName(name);
        if(project == null){
            throw new ResourceNotFoundException("Project '" + name + "' not found!");
        }

        List<Task> taskList = taskRepo.findTaskByProject(project);
        List<TaskDto> taskDtoList = new ArrayList<>();
        for(Task task : taskList){
            taskDtoList.add(TaskService.taskToDTO(task));
        }

        return taskDtoList;
    }



    @Transactional
    @Override
    public List<TaskDto> getTasksByStatus(String status, UserDetails currentUser) throws Exception {

        UserAccount user = userRepo.findUserAccountByEmail(currentUser.getUsername());
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

        List<Task> taskList = user.getTasks();
        List<TaskDto> taskDtoList = new ArrayList<>();

        for(Task task : taskList){
            if(task.getStatus().equals(taskStatus)){
                taskDtoList.add(TaskService.taskToDTO(task));
            }
        }

        return taskDtoList;
    }


    @Transactional
    @Override
    public List<TaskDto> getExpiredTasks(UserDetails currentUser){

        long millis = System.currentTimeMillis();
        Date currentDate = new Date(millis);

        UserAccount user = userRepo.findUserAccountByEmail(currentUser.getUsername());
        List<Task> taskList = user.getTasks();
        List<TaskDto> expiredTasks = new ArrayList<>();

        for(Task task : taskList){
            if(task.getDueDate() != null){
                if(task.getDueDate().before(currentDate)){
                    expiredTasks.add(TaskService.taskToDTO(task));
                }
            }
        }

        return expiredTasks;
    }



    private boolean validUserOfTask(String username, Long id){
        UserAccount user = userRepo.findUserAccountByEmail(username);
        List<Task> userTaskList = user.getTasks();
        Task task = taskRepo.findTaskById(id);
        if(task != null && userTaskList != null){
            if(userTaskList.contains(task)){
                return true;
            }
        }
        return false;
    }

    private boolean validUserOfProject(String username, String projectName){
        UserAccount user = userRepo.findUserAccountByEmail(username);
        List<Project> userProjectList = user.getProjects();
        Project project = projectRepo.findProjectByName(projectName);
        if(project != null && userProjectList != null){
            if(userProjectList.contains(project)){
                return true;
            }
        }
        return false;
    }






}
