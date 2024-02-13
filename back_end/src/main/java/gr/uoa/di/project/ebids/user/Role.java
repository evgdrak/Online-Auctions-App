package gr.uoa.di.project.ebids.user;

import org.springframework.security.core.GrantedAuthority;

/* * * * * * * * * * * * * * * * * * * * *
* Roles of users
* Used for spring security authorization
* * * * * * * * * * * * * * * * * * * * */

public enum Role implements GrantedAuthority {

    Admin("ADMIN"), User("USER");

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + authority;
    }

}
