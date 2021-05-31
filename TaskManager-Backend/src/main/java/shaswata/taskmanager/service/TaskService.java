package shaswata.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import shaswata.taskmanager.repository.AdminRepository;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;

import javax.transaction.Transactional;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TaskService {

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    AdminRepository adminRepo;

    @Autowired
    TaskRepository taskRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ProjectService projectService;



    /////////////////////////////METHODS CALLED BY CONTROLLER///////////////////////////////////////////////////////


    @Transactional
    public TaskDto createTaskService(TaskDto taskDto, UserDetails currentUser) throws Exception {
        TaskDto returnDto;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            returnDto = this.createTask(taskDto);
        } else {
            if(projectService.validUserOfProject(currentUser.getUsername(), taskDto.getProjectName())){
                returnDto = this.createTask(taskDto);
            }else {
                throw new AccessDeniedException("User can only add tasks to own projects!");
            }
        }
        return returnDto;
    }



    @Transactional
    public TaskDto editTaskService(Long id, TaskDto taskDto, UserDetails currentUser) throws Exception {
        TaskDto returnDto;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            returnDto = this.editTask(id, taskDto);
        } else {
            if(validUserOfTask(currentUser.getUsername(), id)){
                returnDto = this.editTask(id, taskDto);
            }else {
                throw new AccessDeniedException("User can only edit own tasks!");
            }
        }
        return returnDto;
    }


    @Transactional
    public TaskDto getTaskService(Long id, UserDetails currentUser) throws Exception {
        TaskDto returnDto;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            returnDto = this.getTask(id);
        } else {
            if(validUserOfTask(currentUser.getUsername(), id)){
                returnDto = this.getTask(id);
            }else {
                throw new AccessDeniedException("User can only view own tasks!");
            }
        }
        return returnDto;
    }


    @Transactional
    public List<TaskDto> getAllTasksService(UserDetails currentUser){
        List<TaskDto> returnDtoList;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            returnDtoList = this.getAllTasks();
        } else {
            returnDtoList = this.getAllTasks(currentUser.getUsername());
        }
        return returnDtoList;
    }


    @Transactional
    public List<TaskDto> getTasksByProjectService(String projectName, UserDetails currentUser) throws Exception {
        List<TaskDto> returnDtoList;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            returnDtoList = this.getTasksByProject(projectName);
        } else {
            if(projectService.validUserOfProject(currentUser.getUsername(), projectName)){
                returnDtoList = this.getTasksByProject(projectName);
            }else {
                throw new AccessDeniedException("User can only access own projects!");
            }
        }
        return returnDtoList;
    }


    @Transactional
    public List<TaskDto> getTasksByStatusService(String status, UserDetails currentUser) throws Exception {
        List<TaskDto> returnDtoList;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            returnDtoList = this.getTasksByStatus(status);
        } else {
            returnDtoList = this.getTasksByStatus(status, currentUser.getUsername());
        }
        return returnDtoList;
    }


    @Transactional
    public List<TaskDto> getExpiredTasksService(UserDetails currentUser) throws Exception {
        List<TaskDto> returnDtoList;
        if(adminRepo.findAdminAccountByEmail(currentUser.getUsername()) != null){
            returnDtoList = this.getExpiredTasks();
        } else {
            returnDtoList = this.getExpiredTasks(currentUser.getUsername());
        }
        return returnDtoList;
    }



    ///////////////////ACTUAL SERVICE METHODS//////////////////////////////////////////////////////


    @Transactional
    public TaskDto createTask(TaskDto taskDto) throws Exception {

        if(taskDto.getDescription() == "" || taskDto.getStatus() == null || taskDto.getProjectName() == "" || taskDto.getDescription() == null || taskDto.getProjectName() == null){
            throw new InvalidInputException("Task description, status or project name cannot be empty!");
        }

        Task task = new Task();
        Project project = projectRepo.findProjectByName(taskDto.getProjectName());
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

        TaskDto returnDto = taskToDTO(task);
        return returnDto;

    }


    @Transactional
    public TaskDto editTask(Long id, TaskDto taskDto) throws Exception {
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

        //Note : Task's project cannot be changed
        task.setDescription(taskDto.getDescription());
        task.setStatus(taskDto.getStatus());
        task.setDueDate(taskDto.getDueDate());
        task = taskRepo.save(task);

        return taskToDTO(task);
    }



    @Transactional
    public TaskDto getTask(Long id) throws Exception {
        if(id == null){
            throw new InvalidInputException("Task ID cannot be empty!");
        }

        Task task = taskRepo.findTaskById(id);
        if(task == null){
            throw new ResourceNotFoundException("Task with given ID not found");
        }

        return taskToDTO(task);
    }


    @Transactional
    public List<TaskDto> getAllTasks(){
        List<Task> taskList = taskRepo.findAll();
        List<TaskDto> taskDtoList = new ArrayList<>();

        for (Task task : taskList){
            taskDtoList.add(taskToDTO(task));
        }

        return taskDtoList;
    }


    @Transactional
    public List<TaskDto> getAllTasks(String username){
        UserAccount user = userRepo.findUserAccountByEmail(username);
        List<Task> taskList = user.getTasks();
        List<TaskDto> taskDtoList = new ArrayList<>();

        for (Task task : taskList){
            taskDtoList.add(taskToDTO(task));
        }

        return taskDtoList;
    }


    @Transactional
    public List<TaskDto> getTasksByProject(String name) throws Exception {
        if(name == null || name == ""){
            throw new InvalidInputException("Project name cannot be empty!");
        }
        Project project = projectRepo.findProjectByName(name);
        if(project == null){
            throw new ResourceNotFoundException("Project '" + name + "' not found!");
        }

        List<Task> taskList = taskRepo.findTaskByProject(project);
        List<TaskDto> taskDtoList = new ArrayList<>();
        for(Task task : taskList){
            taskDtoList.add(taskToDTO(task));
        }

        return taskDtoList;
    }


    @Transactional
    public List<TaskDto> getTasksByStatus(String status) throws Exception {
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
        List<TaskDto> taskDtoList = new ArrayList<>();
        for(Task task : taskList){
            taskDtoList.add(taskToDTO(task));
        }

        return taskDtoList;
    }


    @Transactional
    public List<TaskDto> getTasksByStatus(String username, String status) throws Exception {
        UserAccount user = userRepo.findUserAccountByEmail(username);
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
                taskDtoList.add(taskToDTO(task));
            }
        }

        return taskDtoList;
    }



    @Transactional
    public List<TaskDto> getExpiredTasks(){
        long millis = System.currentTimeMillis();
        Date currentDate = new Date(millis);

        List<Task> taskList = taskRepo.findAll();
        List<TaskDto> expiredTasks = new ArrayList<>();

        for(Task task : taskList){
            if(task.getDueDate() != null){
                if(task.getDueDate().before(currentDate)){
                    expiredTasks.add(taskToDTO(task));
                }
            }
        }

        return expiredTasks;
    }


    @Transactional
    public List<TaskDto> getExpiredTasks(String username){

        long millis = System.currentTimeMillis();
        Date currentDate = new Date(millis);

        UserAccount user = userRepo.findUserAccountByEmail(username);
        List<Task> taskList = user.getTasks();
        List<TaskDto> expiredTasks = new ArrayList<>();

        for(Task task : taskList){
            if(task.getDueDate() != null){
                if(task.getDueDate().before(currentDate)){
                    expiredTasks.add(taskToDTO(task));
                }
            }
        }

        return expiredTasks;
    }

    public boolean validUserOfTask(String username, Long id){
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

    public static TaskDto taskToDTO(Task task){
        TaskDto taskDto = new TaskDto();
        taskDto.setDescription(task.getDescription());
        taskDto.setDueDate(task.getDueDate());
        taskDto.setStatus(task.getStatus());
        taskDto.setProjectName(task.getProject().getName());
        taskDto.setId(task.getId());
        return taskDto;
    }
}
