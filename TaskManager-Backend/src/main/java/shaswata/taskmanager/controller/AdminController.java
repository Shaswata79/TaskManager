package shaswata.taskmanager.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.TaskStatus;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.service.ProjectService;
import shaswata.taskmanager.service.TaskService;
import shaswata.taskmanager.service.UserService;
import java.util.List;


@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("/api/admin")
public class AdminController extends BaseController{

    @Autowired
    UserService userService;



    @GetMapping("/projects_by_user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getProjectsByUser(@RequestParam("email") String email) throws Exception {

        List<ProjectDto> userProjectList = userService.getProjectsByUser(email);
        return new ResponseEntity<>(userProjectList, HttpStatus.OK);


    }


    @GetMapping("/tasks_by_user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getTasksByUser(@RequestParam("email") String email) throws Exception{

        List<TaskDto> userTaskList = userService.getTasksByUser(email);
        return new ResponseEntity<>(userTaskList, HttpStatus.OK);

    }




    @GetMapping("/all_users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(){

        List<UserDto> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);

    }



}


