package shaswata.taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.service.UserService;



@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("api/user")
public class UserController extends BaseController{

    @Autowired
    UserService userService;



    @GetMapping("/hello")
    public ResponseEntity<?> hello(){
        try{
            return new ResponseEntity<>("Hello World", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }




    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) throws Exception {

        userDto = userService.createUser(userDto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }




    @PutMapping("/change_password")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> changePassword(@RequestParam("newPassword") String newPassword) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        UserDto userDto = userService.changePassword(userDetails.getUsername(), newPassword);
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }






    @PostMapping("/assign_to_project")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> assignUserToProject(@RequestParam("username") String email, @RequestParam("projectName") String projectName) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        String message = userService.assignToProjectService(email, projectName, userDetails);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }


    @PostMapping("/assign_to_task")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> assignUserToProject(@RequestParam("username") String email, @RequestParam("taskID") Long id) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        String message = userService.assignToTaskService(email, id, userDetails);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }




    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> getUser(@PathVariable("username") String email) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        UserDto userDto = userService.getUserService(email, userDetails);
        return new ResponseEntity<>(userDto, HttpStatus.OK);

    }



}

