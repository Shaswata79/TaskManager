package shaswata.taskmanager.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler({InvalidInputException.class, BadCredentialsException.class, ResourceNotFoundException.class, DuplicateEntityException.class, UsernameNotFoundException.class})
    public ResponseEntity<?> handleBadRequestExceptions(Exception e, WebRequest request) {
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        System.out.println("Here AccessDeniedException");
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
    }



}
