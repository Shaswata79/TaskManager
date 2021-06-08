package shaswata.taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.service.ServiceFactory;
import shaswata.taskmanager.service.TaskService;

import java.util.List;

@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("/api/task")
public class TaskController extends BaseController{


    private TaskService taskService;
    private final ServiceFactory serviceFactory;


    @Autowired
    public TaskController(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }


    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        taskService = serviceFactory.getTaskService(userDetails);
        taskDto = taskService.createTask(taskDto, userDetails);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);

    }



    @PutMapping("/edit/{id}")
    public ResponseEntity<?> editTask(@PathVariable("id") Long id, @RequestBody TaskDto taskDto) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        taskService = serviceFactory.getTaskService(userDetails);
        taskDto = taskService.editTask(id, taskDto,userDetails);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);

    }



    @GetMapping("/get/{id}")
    public ResponseEntity<?> getTask(@PathVariable("id") Long id) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        taskService = serviceFactory.getTaskService(userDetails);
        TaskDto taskDto = taskService.getTask(id, userDetails);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);

    }



    @GetMapping("/all")
    public ResponseEntity<?> getAllTasks() throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        taskService = serviceFactory.getTaskService(userDetails);
        List<TaskDto> taskList = taskService.getAllTasks(userDetails);
        return new ResponseEntity<>(taskList, HttpStatus.OK);

    }



    @GetMapping("/all_by_project")
    public ResponseEntity<?> getAllTasksByProject(@RequestParam String projectName) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        taskService = serviceFactory.getTaskService(userDetails);
        List<TaskDto> taskDtoList = taskService.getTasksByProject(projectName, userDetails);
        return new ResponseEntity<>(taskDtoList, HttpStatus.OK);

    }



    @GetMapping("/all_expired_tasks")
    public ResponseEntity<?> getAllExpiredTasks() throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        taskService = serviceFactory.getTaskService(userDetails);
        List<TaskDto> taskList = taskService.getExpiredTasks(userDetails);
        return new ResponseEntity<>(taskList, HttpStatus.OK);

    }



    @GetMapping("/all_by_status")
    public ResponseEntity<?> getAllTasksByStatus(@RequestParam String status) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        taskService = serviceFactory.getTaskService(userDetails);
        List<TaskDto> taskList = taskService.getTasksByStatus(status, userDetails);
        return new ResponseEntity<>(taskList, HttpStatus.OK);

    }


}




