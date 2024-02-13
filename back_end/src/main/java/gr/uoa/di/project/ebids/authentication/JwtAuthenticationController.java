package gr.uoa.di.project.ebids.authentication;

import gr.uoa.di.project.ebids.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* * * * * * * * * * * * * * * * * *
 * Controller for login and register
 * * * * * * * * * * * * * * * * * */

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    private static final String password_regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,20}$";
    private static final String email_regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        if(!userService.getUserByUsername(authenticationRequest.getUsername()).isVerified()){
            return new ResponseEntity("User is not verified yet.", HttpStatus.BAD_REQUEST);
        }

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> saveUser(@RequestBody RegisterUser user) {
        if(userService.findIfUserExists(user.getUsername())){
            return new ResponseEntity("Username already exists", HttpStatus.BAD_REQUEST);
        } else if(!isValidPassword(user.getPassword())){
            return new ResponseEntity("Password must be more than 8 characters, have at least one uppercase and lowercase character and a number", HttpStatus.BAD_REQUEST);
        } else if(!isValidEmail(user.getEmail())){
            return new ResponseEntity("Incorrect email", HttpStatus.BAD_REQUEST);
        } else if(!(user.getPhone().matches("[0-9]+") && user.getPhone().length() == 10)){
            return new ResponseEntity("Incorrect phone number", HttpStatus.BAD_REQUEST);
        } else if(!(user.getTIN().matches("[0-9]+") && user.getTIN().length() == 9)){
            return new ResponseEntity("Incorrect TIN", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(userDetailsService.save(user), HttpStatus.OK);
    }

    public static boolean isValidPassword(String password)
    {
        Pattern pattern = Pattern.compile(password_regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidEmail(String email)
    {
        Pattern pattern = Pattern.compile(email_regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}