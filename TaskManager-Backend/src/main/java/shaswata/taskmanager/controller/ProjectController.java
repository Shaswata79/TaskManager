package shaswata.taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.service.AuthenticationService;
import shaswata.taskmanager.service.ProjectService;
import shaswata.taskmanager.service.UserService;

import java.util.List;


@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    UserService userService;



    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto, @RequestHeader String token){
        try{
            if (authenticationService.validateUserToken(token) != null) {
                UserAccount user = authenticationService.validateUserToken(token);
                projectDto = projectService.createProject(projectDto);
                userService.assignUserToProject(user.getEmail(), projectDto.getName());  //assign the current user to project automatically
                return new ResponseEntity<>(projectDto, HttpStatus.OK);

            } else if(authenticationService.validateAdminToken(token) != null){
                projectDto = projectService.createProject(projectDto);
                return new ResponseEntity<>(projectDto, HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Must be logged in to create a project.", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllProjects(@RequestHeader String token){
        try{

            if (authenticationService.validateUserToken(token) != null) {
                UserAccount user = authenticationService.validateUserToken(token);
                List<ProjectDto> projectDtoList = projectService.getAllProjectsByUser(user);
                return new ResponseEntity<>(projectDtoList, HttpStatus.OK);

            }else if(authenticationService.validateAdminToken(token) != null){
                List<ProjectDto> projectDtoList = projectService.getAllProjects();
                return new ResponseEntity<>(projectDtoList, HttpStatus.OK);

            } else{
                return new ResponseEntity<>("Must be logged in to view all projects.", HttpStatus.BAD_REQUEST);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteProject(@PathVariable("name") String projectName, @RequestHeader String token) {
        try {

            if (authenticationService.validateAdminToken(token) != null) {
                String message = projectService.deleteProject(projectName);
                return new ResponseEntity<>(message, HttpStatus.OK);

            } else if(authenticationService.validateUserToken(token) != null){
                UserAccount user = authenticationService.validateUserToken(token);
                String message = projectService.deleteProjectByUser(projectName, user);
                return new ResponseEntity<>(message, HttpStatus.OK);

            } else{
                return new ResponseEntity<>("Must be logged in to view all projects.", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }










}
