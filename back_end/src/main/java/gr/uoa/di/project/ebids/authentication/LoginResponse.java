package gr.uoa.di.project.ebids.authentication;

import java.io.Serializable;

/* * * * * * * * * * * * * * *
 * Class for response of login
 * * * * * * * * * * * * * * */

public class LoginResponse implements Serializable {

    private final String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}