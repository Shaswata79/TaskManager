package shaswata.taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.ProjectDto;
import shaswata.taskmanager.service.ProjectService;
import shaswata.taskmanager.service.ServiceFactory;

import java.util.List;


@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("/api/project")
public class ProjectController extends BaseController{


    private ProjectService projectService;
    private final ServiceFactory serviceFactory;


    @Autowired
    public ProjectController(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }




    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestBody ProjectDto projectDto) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        projectService = serviceFactory.getProjectService(userDetails);
        projectDto = projectService.createProject(projectDto, userDetails);
        return new ResponseEntity<>(projectDto, HttpStatus.OK);

    }



    @GetMapping("/all")
    public ResponseEntity<?> getAllProjects() throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        projectService = serviceFactory.getProjectService(userDetails);
        List<ProjectDto> projectDtoList = projectService.getAllProjects(userDetails);
        return new ResponseEntity<>(projectDtoList, HttpStatus.OK);

    }



    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteProject(@PathVariable("name") String projectName) throws Exception {

        UserDetails userDetails = super.getCurrentUser();
        projectService = serviceFactory.getProjectService(userDetails);
        String message = projectService.deleteProject(projectName, userDetails);
        return new ResponseEntity<>(message, HttpStatus.OK);

    }



}





