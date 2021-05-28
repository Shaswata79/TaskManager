package shaswata.taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;
//import shaswata.taskmanager.service.AuthenticationService;
import shaswata.taskmanager.service.ProjectService;
import shaswata.taskmanager.service.TaskService;
import shaswata.taskmanager.service.UserService;

import java.util.List;


@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("api/user")
public class UserController extends BaseController{

    @Autowired
    UserService userService;

    @Autowired
    ProjectService projectService;

    //TODO Remove all autowired repositories

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    TaskRepository taskRepo;

    @Autowired
    UserRepository userRepo;


    @GetMapping("/hello")
    public ResponseEntity<?> hello(){
        try{
            return new ResponseEntity<>("Hello World", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }




    @PostMapping("/register")
    public ResponseEntity<?> createCustomer(@RequestBody UserDto userDto){
        try {
            userDto = userService.createUser(userDto);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }




    @PutMapping("/change_password")
    public ResponseEntity<?> changePassword(@RequestParam("newPassword") String newPassword){
        try{
            UserDetails userDetails = super.getCurrentUser();
            UserDto userDto = userService.changePassword(userDetails.getUsername(), newPassword);
            return new ResponseEntity<>(userDto, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }






    @PostMapping("/assign_to_project")
    public ResponseEntity<?> assignUserToProject(@RequestParam("username") String email, @RequestParam("projectName") String projectName){
        try{
            UserDetails userDetails = super.getCurrentUser();
            UserAccount user = userRepo.findUserAccountByEmail(userDetails.getUsername());
            if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) || projectService.validUserOfProject(user, projectName)) {
                //either the current user is the admin or the current user is a valid user of the project
                String message = userService.assignUserToProject(email, projectName);
                return new ResponseEntity<>(message, HttpStatus.OK);

            } else{
                return new ResponseEntity<>("Must be a valid user of the project", HttpStatus.OK);
            }

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/assign_to_task")
    public ResponseEntity<?> assignUserToProject(@RequestParam("username") String email, @RequestParam("taskID") Long id){
        try{
            UserDetails userDetails = super.getCurrentUser();
            UserAccount user = userRepo.findUserAccountByEmail(userDetails.getUsername());
            Task task = taskRepo.findTaskById(id);
            if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) || projectService.validUserOfProject(user, task.getProject().getName())) {
                String message = userService.assignUserToTask(email, id);
                return new ResponseEntity<>(message, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Must be a valid user of the project!", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(){
        try{
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                List<UserDto> userList = userService.getAllUsers();
                return new ResponseEntity<>(userList, HttpStatus.OK);

            } else{
                return new ResponseEntity<>("Must be logged in as admin to view all users.", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/get")
    public ResponseEntity<?> getUser(@PathVariable("username") String email){
        try{
            UserDetails userDetails = super.getCurrentUser();
            if(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")) || ((userDetails != null) && (userDetails.getUsername() == email))){
                //either the current user is the admin or the current user is trying to view own account details
                UserDto userDto = userService.getUser(email);
                return new ResponseEntity<>(userDto, HttpStatus.OK);

            } else{
                return new ResponseEntity<>("Must be logged in as valid user to view account details!", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }








}

