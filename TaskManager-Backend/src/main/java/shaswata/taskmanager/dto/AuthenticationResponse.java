package shaswata.taskmanager.dto;

import shaswata.taskmanager.common.ApplicationUserRole;

import java.io.Serializable;

public class AuthenticationResponse implements Serializable {

    private final String jwt;
    private final String userType;



    public AuthenticationResponse(String jwt, String userType) {
        this.jwt = jwt;
        this.userType = userType;
    }


    public String getJwt() {
        return jwt;
    }
    public String getUserType() {
        return userType;
    }

}
