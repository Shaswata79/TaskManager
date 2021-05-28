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
//import shaswata.taskmanager.service.AuthenticationService;
import shaswata.taskmanager.service.UserService;

import java.util.List;


@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("/api/admin")
public class AdminController extends BaseController{

    @Autowired
    UserService userService;


    @GetMapping("/projects_by_user")
    public ResponseEntity<?> getProjectsByUser(@RequestParam("email") String email){
        try{
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                List<ProjectDto> userProjectList = userService.getProjectsByUser(email);
                return new ResponseEntity<>(userProjectList, HttpStatus.OK);

            } else{
                return new ResponseEntity<>("Must be logged in as admin", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/tasks_by_user")
    public ResponseEntity<?> getTasksByUser(@RequestParam("email") String email){
        try{
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                List<TaskDto> userTaskList = userService.getTasksByUser(email);
                return new ResponseEntity<>(userTaskList, HttpStatus.OK);
            } else{
                return new ResponseEntity<>("Must be logged in as admin!", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}


