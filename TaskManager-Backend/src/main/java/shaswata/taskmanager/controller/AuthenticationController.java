package shaswata.taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import shaswata.taskmanager.dto.AuthenticationRequest;
import shaswata.taskmanager.dto.AuthenticationResponse;
import shaswata.taskmanager.service.AuthenticationService;




@CrossOrigin(origins = "*")   //enable resource sharing among other domain (eg: the frontend host server)
@RestController
@RequestMapping("api/authentication")
public class AuthenticationController{



    @Autowired
    private AuthenticationService authenticationService;


    @GetMapping(value = "/hello")
    public ResponseEntity<?> sayHello() throws Exception{
        return new ResponseEntity<>("Hello World", HttpStatus.OK);
    }


    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{

        AuthenticationResponse authenticationResponse = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(authenticationResponse);

    }



    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        try {
            SecurityContextHolder.getContext().setAuthentication(null);
            return new ResponseEntity<>("You have been logged out", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}



