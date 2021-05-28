package shaswata.taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.ProjectService;
import shaswata.taskmanager.service.TaskService;

import java.util.List;

@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("api/task")
public class TaskController extends BaseController{

    @Autowired
    TaskService taskService;

    @Autowired
    ProjectService projectService;

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    TaskRepository taskRepo;

    @Autowired
    UserRepository userRepo;



    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto){

        try{
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                taskDto = taskService.createTask(taskDto);
                return new ResponseEntity<>(taskDto, HttpStatus.OK);

            } else {
                UserAccount user = userRepo.findUserAccountByEmail(userDetails.getUsername());
                if(projectService.validUserOfProject(user, taskDto.getProjectName())){
                    taskDto = taskService.createTask(taskDto);
                    return new ResponseEntity<>(taskDto, HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("User can only add tasks to own projects!", HttpStatus.BAD_REQUEST);
                }
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editTask(@PathVariable("id") Long id, @RequestBody TaskDto taskDto){

        try{
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                taskDto = taskService.editTask(id, taskDto);
                return new ResponseEntity<>(taskDto, HttpStatus.OK);
            } else{
                UserAccount user = userRepo.findUserAccountByEmail(userDetails.getUsername());
                if(taskService.validUserOfTask(user, id)){
                    taskDto = taskService.editTask(id, taskDto);
                    return new ResponseEntity<>(taskDto, HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("User can only edit own tasks!", HttpStatus.BAD_REQUEST);
                }
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") Long id){

        try{
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                TaskDto taskDto = taskService.getTask(id);
                return new ResponseEntity<>(taskDto, HttpStatus.OK);

            } else{
                UserAccount user = userRepo.findUserAccountByEmail(userDetails.getUsername());
                if(taskService.validUserOfTask(user, id)){
                    TaskDto taskDto = taskService.getTask(id);
                    return new ResponseEntity<>(taskDto, HttpStatus.OK);
                }else {
                    return new ResponseEntity<>("User can only view own tasks!", HttpStatus.BAD_REQUEST);
                }
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/all")
    public ResponseEntity<?> getAllTasks(){

        try{
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                List<TaskDto> taskList = taskService.getAllTasks();
                return new ResponseEntity<>(taskList, HttpStatus.OK);

            } else{
                UserAccount user = userRepo.findUserAccountByEmail(userDetails.getUsername());
                List<TaskDto> taskList = taskService.getAllTasks(user);
                return new ResponseEntity<>(taskList, HttpStatus.OK);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



    @GetMapping("/all_by_project")
    public ResponseEntity<?> getAllTasksByProject(@RequestParam String projectName){

        try{
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                List<TaskDto> taskDtoList = taskService.getTasksByProject(projectName);
                return new ResponseEntity<>(taskDtoList, HttpStatus.OK);

            } else {
                UserAccount user = userRepo.findUserAccountByEmail(userDetails.getUsername());
                if(projectService.validUserOfProject(user, projectName)){
                    List<TaskDto> taskDtoList = taskService.getTasksByProject(projectName);
                    return new ResponseEntity<>(taskDtoList, HttpStatus.OK);
                } else{
                    return new ResponseEntity<>("User can only access own projects!", HttpStatus.BAD_REQUEST);
                }
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



    @GetMapping("/all_expired_tasks")
    public ResponseEntity<?> getAllExpiredTasks(){

        try{
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                List<TaskDto> taskList = taskService.getExpiredTasks();
                return new ResponseEntity<>(taskList, HttpStatus.OK);

            } else{
                UserAccount user = userRepo.findUserAccountByEmail(userDetails.getUsername());
                List<TaskDto> taskList = taskService.getExpiredTasks(user);
                return new ResponseEntity<>(taskList, HttpStatus.OK);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



    @GetMapping("/all_by_status")
    public ResponseEntity<?> getAllTasksByStatus(@RequestParam String status){
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
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                List<TaskDto> taskList = taskService.getTasksByStatus(taskStatus);
                return new ResponseEntity<>(taskList, HttpStatus.OK);

            }else{
                UserAccount user = userRepo.findUserAccountByEmail(userDetails.getUsername());
                List<TaskDto> taskList = taskService.getTasksByStatus(user, taskStatus);
                return new ResponseEntity<>(taskList, HttpStatus.OK);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


}


