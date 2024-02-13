package gr.uoa.di.project.ebids.authentication;

import java.io.Serializable;

/* * * * * * * * * * * * * *
 * Class for body of login
 * * * * * * * * * * * * * */

public class LoginRequest implements Serializable {
    private String username;
    private String password;

    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}