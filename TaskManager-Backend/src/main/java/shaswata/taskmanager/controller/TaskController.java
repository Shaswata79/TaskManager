package shaswata.taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.service.AuthenticationService;
import shaswata.taskmanager.service.ProjectService;
import shaswata.taskmanager.service.TaskService;

import java.util.List;

@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    ProjectService projectService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    TaskRepository taskRepo;



    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto, @RequestHeader String token){

        try{
            if (authenticationService.validateUserToken(token) != null) {
                UserAccount user = authenticationService.validateUserToken(token);
                if(validUserOfProject(user, taskDto.getProjectName())){
                    taskDto = taskService.createTask(taskDto);
                    return new ResponseEntity<>(taskDto, HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("User can only add tasks to own projects!", HttpStatus.BAD_REQUEST);
                }

            }else if(authenticationService.validateAdminToken(token) != null){
                taskDto = taskService.createTask(taskDto);
                return new ResponseEntity<>(taskDto, HttpStatus.OK);
            } else{
                return new ResponseEntity<>("Must be logged in to create a task.", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editTask(@PathVariable("id") Long id, @RequestBody TaskDto taskDto, @RequestHeader String token){

        try{
            if (authenticationService.validateUserToken(token) != null) {
                UserAccount user = authenticationService.validateUserToken(token);
                if(validUserOfTask(user, id)){
                    taskDto = taskService.editTask(id, taskDto);
                    return new ResponseEntity<>(taskDto, HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("User can only edit own tasks!", HttpStatus.BAD_REQUEST);
                }

            }else if(authenticationService.validateAdminToken(token) != null){
                taskDto = taskService.editTask(id, taskDto);
                return new ResponseEntity<>(taskDto, HttpStatus.OK);

            } else{
                return new ResponseEntity<>("Must be logged in to edit a task.", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") Long id, @RequestHeader String token){

        try{
            if (authenticationService.validateUserToken(token) != null) {
                UserAccount user = authenticationService.validateUserToken(token);
                if(validUserOfTask(user, id)){
                    TaskDto taskDto = taskService.getTask(id);
                    return new ResponseEntity<>(taskDto, HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("User can only view own tasks!", HttpStatus.BAD_REQUEST);
                }

            }else if(authenticationService.validateAdminToken(token) != null){
                TaskDto taskDto = taskService.getTask(id);
                return new ResponseEntity<>(taskDto, HttpStatus.OK);

            } else{
                return new ResponseEntity<>("Must be logged in to view a task.", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/all")
    public ResponseEntity<?> getAllTasks(@RequestHeader String token){

        try{
            if (authenticationService.validateUserToken(token) != null) {
                UserAccount user = authenticationService.validateUserToken(token);
                List<TaskDto> taskList = taskService.getAllTasksByUser(user);
                return new ResponseEntity<>(taskList, HttpStatus.OK);

            }else if(authenticationService.validateAdminToken(token) != null){
                List<TaskDto> taskList = taskService.getAllTasks();
                return new ResponseEntity<>(taskList, HttpStatus.OK);

            } else{
                return new ResponseEntity<>("Must be logged in view all tasks.", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/all_by_project")
    public ResponseEntity<?> getAllTasksByProject(@RequestParam String projectName, @RequestHeader String token){

        try{
            if (authenticationService.validateUserToken(token) != null) {
                UserAccount user = authenticationService.validateUserToken(token);
                if(validUserOfProject(user, projectName)){
                    List<TaskDto> taskDtoList = taskService.getTasksByProject(projectName);
                    return new ResponseEntity<>(taskDtoList, HttpStatus.OK);
                } else{
                    return new ResponseEntity<>("User can only access own projects!", HttpStatus.BAD_REQUEST);
                }

            }else if(authenticationService.validateAdminToken(token) != null){
                List<TaskDto> taskDtoList = taskService.getTasksByProject(projectName);
                return new ResponseEntity<>(taskDtoList, HttpStatus.OK);

            } else{
                return new ResponseEntity<>("Must be logged in view all tasks of a project.", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



    @GetMapping("/all_expired_tasks")
    public ResponseEntity<?> getAllExpiredTasks(@RequestHeader String token){

        try{
            if (authenticationService.validateUserToken(token) != null) {
                UserAccount user = authenticationService.validateUserToken(token);
                List<TaskDto> taskList = taskService.getExpiredTasks(user);
                return new ResponseEntity<>(taskList, HttpStatus.OK);

            }else if(authenticationService.validateAdminToken(token) != null){
                List<TaskDto> taskList = taskService.getExpiredTasks();
                return new ResponseEntity<>(taskList, HttpStatus.OK);

            } else{
                return new ResponseEntity<>("Must be logged in view tasks.", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



    @GetMapping("/all_by_status")
    public ResponseEntity<?> getAllTasksByStatus(@RequestParam String status, @RequestHeader String token){
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
                return new ResponseEntity<>("Not a valid task status", HttpStatus.BAD_REQUEST);
        }

        try{
            if (authenticationService.validateUserToken(token) != null) {
                UserAccount user = authenticationService.validateUserToken(token);
                List<TaskDto> taskList = taskService.getTasksByStatus(user, taskStatus);
                return new ResponseEntity<>(taskList, HttpStatus.OK);

            }else if(authenticationService.validateAdminToken(token) != null){
                List<TaskDto> taskList = taskService.getTasksByStatus(taskStatus);
                return new ResponseEntity<>(taskList, HttpStatus.OK);

            } else{
                return new ResponseEntity<>("Must be logged in view tasks.", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }






    private boolean validUserOfProject(UserAccount user, String projectName){
        List<Project> userProjectList = user.getProjects();
        Project project = projectRepo.findProjectByName(projectName);
        if(project != null && userProjectList != null){
            if(userProjectList.contains(project)){
                return true;
            }
        }
        return false;
    }


    private boolean validUserOfTask(UserAccount user, Long id){
        List<Task> userTaskList = user.getTasks();
        Task task = taskRepo.findTaskById(id);
        if(task != null && userTaskList != null){
            if(userTaskList.contains(task)){
                return true;
            }
        }
        return false;
    }

}
