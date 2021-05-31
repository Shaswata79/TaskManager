package shaswata.taskmanager.controller;


import jdk.internal.vm.compiler.collections.EconomicMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        projectDto = projectService.createProjectService(projectDto, userDetails);
        return new ResponseEntity<>(projectDto, HttpStatus.OK);

    }



    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> getAllProjects() {

        UserDetails userDetails = super.getCurrentUser();
        List<ProjectDto> projectDtoList = projectService.getAllProjectService(userDetails);
        return new ResponseEntity<>(projectDtoList, HttpStatus.OK);

    }



    @DeleteMapping("/delete/{name}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<?> deleteProject(@PathVariable("name") String projectName) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        String message = projectService.deleteProjectService(projectName, userDetails);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }



}



