
package shaswata.taskmanager.service.authentication;

import org.springframework.stereotype.Service;
import shaswata.taskmanager.dto.AuthenticationRequest;
import shaswata.taskmanager.dto.AuthenticationResponse;




@Service
public interface AuthenticationService {

    AuthenticationResponse login(AuthenticationRequest request) throws Exception;

}



