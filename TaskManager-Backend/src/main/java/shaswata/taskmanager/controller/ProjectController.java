package shaswata.taskmanager.controller;


import jdk.internal.vm.compiler.collections.EconomicMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.model.UserAccount;
import shaswata.taskmanager.repository.UserRepository;
import shaswata.taskmanager.service.ProjectService;
import shaswata.taskmanager.service.UserService;

import java.util.List;


@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("/api/project")
public class ProjectController extends BaseController{

    @Autowired
    ProjectService projectService;


    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;



    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) {

        try{
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {     //current user has role admin
                projectDto = projectService.createProject(projectDto);
                return new ResponseEntity<>(projectDto, HttpStatus.OK);
            } else{
                projectDto = projectService.createProject(projectDto);
                userService.assignUserToProject(userDetails.getUsername(), projectDto.getName());  //assign the current user to project automatically
                return new ResponseEntity<>(projectDto, HttpStatus.OK);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



    @GetMapping("/all")
    public ResponseEntity<?> getAllProjects() {
        try{
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                List<ProjectDto> projectDtoList = projectService.getAllProjects();
                return new ResponseEntity<>(projectDtoList, HttpStatus.OK);
            } else{
                UserAccount user = userRepository.findUserAccountByEmail(userDetails.getUsername());
                List<ProjectDto> projectDtoList = projectService.getAllProjectsByUser(user);
                return new ResponseEntity<>(projectDtoList, HttpStatus.OK);
            }

        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteProject(@PathVariable("name") String projectName) {
        try {
            UserDetails userDetails = super.getCurrentUser();
            if (userDetails != null && userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
                String message = projectService.deleteProject(projectName);
                return new ResponseEntity<>(message, HttpStatus.OK);
            } else{
                UserAccount user = userRepository.findUserAccountByEmail(userDetails.getUsername());
                String message = projectService.deleteProject(projectName, user);
                return new ResponseEntity<>(message, HttpStatus.OK);
            }


        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }



}



