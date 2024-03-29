package gr.uoa.di.project.ebids.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import gr.uoa.di.project.ebids.user.*;

/* * * * * * * * * * * * * *
 * Loads user-specific data
 * * * * * * * * * * * * * */

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDAO userDao;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.getAuthorities());
    }

    public Boolean save(RegisterUser registerUser) {
        User user = new User(registerUser);
        user.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userDao.createUser(user);
    }
}