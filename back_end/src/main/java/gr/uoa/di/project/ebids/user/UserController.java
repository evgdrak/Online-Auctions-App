package gr.uoa.di.project.ebids.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* * * * * * * * * * * * * * * * * * * *
* Controller for requests on user entity
* * * * * * * * * * * * * * * * * * * */

@RestController
public class UserController {
    // To check that email contains right characters
    private static final String email_regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    @Autowired
    UserService userService;

    // Get all users
    @GetMapping(value = "/users", produces="application/json")
    public List<UserWS> getUsers() {
        return userService.findUsers();
    }

    // Add new user
    @PostMapping(value = "/users", consumes = "application/json", produces = "application/json")
    public Boolean addUser(@RequestBody UserWS userWS) {
        return userService.createUser(userWS);
    }

    // Get user by username
    @GetMapping(value = "/users/{id}", produces = "application/json")
    public UserWS getUser(@PathVariable String id) {
        return userService.getUserByUsername(id);
    }

    // Update user with username = id
    @PutMapping(value = "/users/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Boolean> updateUser(@RequestBody UserWS newUser, @PathVariable String id) {

        // Check that user gave correct data
        if(!isValidEmail(newUser.getEmail())){
            return new ResponseEntity("Incorrect email", HttpStatus.BAD_REQUEST);
        } else if(!(newUser.getPhone().matches("[0-9]+") && newUser.getPhone().length() == 10)){
            return new ResponseEntity("Incorrect phone number", HttpStatus.BAD_REQUEST);
        } else if(!(newUser.getTIN().matches("[0-9]+") && newUser.getTIN().length() == 9)){
            return new ResponseEntity("Incorrect TIN", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(userService.updateUser(newUser, id), HttpStatus.OK);
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(email_regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
