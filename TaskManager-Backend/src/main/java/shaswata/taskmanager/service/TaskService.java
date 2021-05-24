package shaswata.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;

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
    TaskRepository taskRepo;


    @Transactional
    public TaskDto createTask(TaskDto taskDto) throws Exception {

        if(taskDto.getDescription() == null || taskDto.getStatus() == null || taskDto.getProjectName() == null){
            throw new Exception("Task description, status or project name cannot be empty!");
        }

        Task task = new Task();
        Project project = projectRepo.findProjectByName(taskDto.getProjectName());
        if(project == null){
            throw new Exception("A task can only be created in an existing project!");
        }
        if(project.getTasks().stream().map(Task::getDescription).collect(Collectors.toList()).contains(taskDto.getDescription())){
            throw new Exception("Task already exists in project");
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
            throw new Exception("Task ID cannot be empty!");
        }
        Task task = taskRepo.findTaskById(id);
        if(task == null){
            throw new Exception("Task with given ID not found");
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
            throw new Exception("Task ID cannot be empty!");
        }

        Task task = taskRepo.findTaskById(id);
        if(task == null){
            throw new Exception("Task with given ID not found");
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
    public List<TaskDto> getAllTasksByUser(UserAccount user){
        List<Task> taskList = user.getTasks();
        List<TaskDto> taskDtoList = new ArrayList<>();

        for (Task task : taskList){
            taskDtoList.add(taskToDTO(task));
        }

        return taskDtoList;
    }


    @Transactional
    public List<TaskDto> getTasksByProject(String name) throws Exception {
        if(name == null){
            throw new Exception("Project name cannot be empty!");
        }
        Project project = projectRepo.findProjectByName(name);
        if(project == null){
            throw new Exception("Project '" + name + "' not found!");
        }

        List<Task> taskList = taskRepo.findTaskByProject(project);
        List<TaskDto> taskDtoList = new ArrayList<>();
        for(Task task : taskList){
            taskDtoList.add(taskToDTO(task));
        }

        return taskDtoList;
    }


    @Transactional
    public List<TaskDto> getTasksByStatus(TaskStatus status) throws Exception {
        if(status == null){
            throw new Exception("Task status cannot be empty!");
        }

        List<Task> taskList = taskRepo.findTaskByStatus(status);
        List<TaskDto> taskDtoList = new ArrayList<>();
        for(Task task : taskList){
            taskDtoList.add(taskToDTO(task));
        }

        return taskDtoList;
    }


    @Transactional
    public List<TaskDto> getTasksByStatus(UserAccount user, TaskStatus status) throws Exception {
        if(status == null){
            throw new Exception("Task status cannot be empty!");
        }

        List<Task> taskList = user.getTasks();
        List<TaskDto> taskDtoList = new ArrayList<>();

        for(Task task : taskList){
            if(task.getStatus().equals(status)){
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
    public List<TaskDto> getExpiredTasks(UserAccount user){
        long millis = System.currentTimeMillis();
        Date currentDate = new Date(millis);

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
