package shaswata.taskmanager.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.dto.TaskDto;
import shaswata.taskmanager.dto.UserDto;
import shaswata.taskmanager.service.AdminService;

import java.util.List;


@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController extends BaseController{


    private final AdminService adminService;


    @GetMapping("/projects_by_user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getProjectsByUser(@RequestParam("email") String email) throws Exception {

        List<ProjectDto> userProjectList = adminService.getProjectsByUser(email);
        return new ResponseEntity<>(userProjectList, HttpStatus.OK);

    }


    @GetMapping("/tasks_by_user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getTasksByUser(@RequestParam("email") String email) throws Exception{

        List<TaskDto> userTaskList = adminService.getTasksByUser(email);
        return new ResponseEntity<>(userTaskList, HttpStatus.OK);

    }


    @GetMapping("/all_users")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers(){

        List<UserDto> userList = adminService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);

    }



}



