
package shaswata.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.AuthenticationRequest;
import shaswata.taskmanager.dto.AuthenticationResponse;
import shaswata.taskmanager.security.JwtUtil;


import javax.transaction.Transactional;

@Service
public class AuthenticationService {

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;


    @Transactional
    public AuthenticationResponse login(AuthenticationRequest request) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        AuthenticationResponse response = new AuthenticationResponse(jwt);
        return response;

    }




}



