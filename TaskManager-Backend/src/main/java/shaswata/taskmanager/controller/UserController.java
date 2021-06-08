package shaswata.taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.service.ServiceFactory;
import shaswata.taskmanager.service.UserService;
import shaswata.taskmanager.service.impl.UserServiceUserImpl;


@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("/api/user")
public class UserController extends BaseController{


    private UserService userService;
    private final ServiceFactory serviceFactory;


    @Autowired
    public UserController(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }


    @GetMapping("/hello")
    public ResponseEntity<?> hello(){
        try{
            return new ResponseEntity<>("Hello World", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) throws Exception {    //TODO: remove redundancy if possible

        UserDetails userDetails = super.getCurrentUser();
        userService = serviceFactory.getUserService(userDetails);
        userDto = userService.createUser(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }


    @PutMapping("/change_password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> changePassword(@RequestParam("newPassword") String newPassword) throws Exception {     //TODO

        UserDetails userDetails = super.getCurrentUser();
        UserServiceUserImpl userServiceUser = serviceFactory.getUserServiceUser();
        UserDto userDto = userServiceUser.changePassword(newPassword, userDetails);
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }



    @PostMapping("/assign_to_project")
    public ResponseEntity<?> assignUserToProject(@RequestParam("username") String email, @RequestParam("projectName") String projectName) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        userService = serviceFactory.getUserService(userDetails);
        String message = userService.assignUserToProject(email, projectName, userDetails);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }


    @PostMapping("/assign_to_task")
    public ResponseEntity<?> assignUserToTask(@RequestParam("username") String email, @RequestParam("taskID") Long id) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        userService = serviceFactory.getUserService(userDetails);
        String message = userService.assignUserToTask(email, id, userDetails);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }


    @GetMapping("/get")
    public ResponseEntity<?> getUser(@PathVariable("username") String email) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        userService = serviceFactory.getUserService(userDetails);
        UserDto userDto = userService.getUser(email, userDetails);
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }



}



