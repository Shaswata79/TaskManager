package shaswata.taskmanager.dto;

import com.sun.istack.NotNull;

import shaswata.taskmanager.service.utilities.Role;

public class LoginDto {

    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private Role role;



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
