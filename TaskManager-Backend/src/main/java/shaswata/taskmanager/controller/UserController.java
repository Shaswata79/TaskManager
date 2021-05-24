package shaswata.taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.model.Project;
import shaswata.taskmanager.model.Task;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.ProjectRepository;
import shaswata.taskmanager.repository.TaskRepository;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.AuthenticationService;
import shaswata.taskmanager.service.TaskService;
import shaswata.taskmanager.service.UserService;

import java.util.List;


@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    ProjectRepository projectRepo;

    @Autowired
    TaskRepository taskRepo;


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
    public ResponseEntity<?> changePassword(@RequestParam("newPassword") String newPassword, @RequestHeader String token){
        try{
            UserAccount user = authenticationService.validateUserToken(token);
            if(user != null){
                UserDto userDto = userService.changePassword(user.getEmail(), newPassword);
                return new ResponseEntity<>(userDto, HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Must be logged in to change password", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/assign_to_project")
    public ResponseEntity<?> assignUserToProject(@RequestParam("email") String email, @RequestParam("projectName") String projectName, @RequestHeader String token){
        try{
            UserAccount user = authenticationService.validateUserToken(token);
            if(authenticationService.validateAdminToken(token) != null || ((user != null) && (validUserOfProject(user, projectName)))){
                //either the current user is the admin or the current user is a valid user of the project
                String message = userService.assignUserToProject(email, projectName);
                return new ResponseEntity<>(message, HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Must be logged in as a valid user of the project!", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/assign_to_task")
    public ResponseEntity<?> assignUserToProject(@RequestParam("email") String email, @RequestParam("taskID") Long id, @RequestHeader String token){
        try{
            UserAccount user = authenticationService.validateUserToken(token);
            Task task = taskRepo.findTaskById(id);
            if(authenticationService.validateAdminToken(token) != null || ((user != null) && (validUserOfProject(user, task.getProject().getName())))){
                //either the current user is the admin or the current user is a valid user of the project
                String message = userService.assignUserToTask(email, id);
                return new ResponseEntity<>(message, HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Must be logged in as a valid user of the project!", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader String token){
        try{
            if(authenticationService.validateAdminToken(token) != null){
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
    public ResponseEntity<?> getUser(@PathVariable("email") String email, @RequestHeader String token){
        try{
            UserAccount user = authenticationService.validateUserToken(token);
            if((authenticationService.validateAdminToken(token) != null) || ((user != null) && (user.getEmail() == email))){
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


}
