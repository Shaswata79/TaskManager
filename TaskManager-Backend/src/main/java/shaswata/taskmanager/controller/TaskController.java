package shaswata.taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.TaskDto;
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



    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> createTask(@RequestBody TaskDto taskDto) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        taskDto = taskService.createTaskService(taskDto, userDetails);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);

    }



    @PutMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> editTask(@PathVariable("id") Long id, @RequestBody TaskDto taskDto) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        taskDto = taskService.editTaskService(id, taskDto,userDetails);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);

    }



    @GetMapping("/get/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> getTask(@PathVariable("id") Long id) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        TaskDto taskDto = taskService.getTaskService(id, userDetails);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);

    }



    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> getAllTasks(){

        UserDetails userDetails = super.getCurrentUser();
        List<TaskDto> taskList = taskService.getAllTasksService(userDetails);
        return new ResponseEntity<>(taskList, HttpStatus.OK);

    }



    @GetMapping("/all_by_project")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> getAllTasksByProject(@RequestParam String projectName) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        List<TaskDto> taskDtoList = taskService.getTasksByProjectService(projectName, userDetails);
        return new ResponseEntity<>(taskDtoList, HttpStatus.OK);

    }



    @GetMapping("/all_expired_tasks")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> getAllExpiredTasks() throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        List<TaskDto> taskList = taskService.getExpiredTasksService(userDetails);
        return new ResponseEntity<>(taskList, HttpStatus.OK);

    }



    @GetMapping("/all_by_status")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> getAllTasksByStatus(@RequestParam String status) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        List<TaskDto> taskList = taskService.getTasksByStatusService(status, userDetails);
        return new ResponseEntity<>(taskList, HttpStatus.OK);

    }


}


