package shaswata.taskmanager.service.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import shaswata.taskmanager.common.ApplicationUserRole;
import shaswata.taskmanager.dto.AuthenticationRequest;
import shaswata.taskmanager.dto.AuthenticationResponse;
import shaswata.taskmanager.exception.InvalidInputException;
import shaswata.taskmanager.security.JwtUtil;
import shaswata.taskmanager.service.MyUserDetailsService;

import javax.transaction.Transactional;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final MyUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;


    @Transactional
    @Override
    public AuthenticationResponse login(AuthenticationRequest request) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        ApplicationUserRole userType = getUserType(userDetails);
        AuthenticationResponse response = new AuthenticationResponse(jwt, userType.name());
        return response;

    }



    private ApplicationUserRole getUserType(UserDetails user) throws InvalidInputException {
         if(user.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))){
             return ApplicationUserRole.ADMIN;
         }
         if(user.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_USER"))){
             return ApplicationUserRole.USER;
         }

         throw new InvalidInputException("No role found for current user");
    }



}
