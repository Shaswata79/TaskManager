package shaswata.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;
import shaswata.taskmanager.security.JwtUtil;
import shaswata.taskmanager.service.MyUserDetailsService;

import javax.servlet.http.HttpServletRequest;


public class BaseController {

    private HttpServletRequest request;
    private MyUserDetailsService userDetailsService;

    @Autowired
    public void inject(HttpServletRequest request, MyUserDetailsService userDetailsService){    //This is needed to initialize the request and userDetailsService
        this.request = request;
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    JwtUtil jwtUtil;

    //@Value("${auth.header.name}")
    //private String authHeaderName;


    protected UserDetails getCurrentUser(){
        String username = null;
        String jwt = null;
        String token = request.getHeader("token");

        if(token != null && token.startsWith("Bearer ")){
            jwt = token.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return userDetails;
    }

}


