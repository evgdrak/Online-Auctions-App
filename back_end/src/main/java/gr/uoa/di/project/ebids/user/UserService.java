package gr.uoa.di.project.ebids.user;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/* * * * * * * * * * * * * * * * *
 * Functionalities for user entity
 * * * * * * * * * * * * * * * * */

@Service
public class UserService {
    @Autowired
    UserDAO userDAO;

    // Get all users
    public List<UserWS> findUsers(){
        List<UserWS> userWS = new ArrayList<>();
        List<User> users = userDAO.findUsers();
        for(User user: users){
            userWS.add(new UserWS(user));
        }
        return userWS;
    }

    // Add new user
    public Boolean createUser(UserWS userWS) {
        return userDAO.createUser(new User(userWS));
    }

    // Get user by username
    public UserWS getUserByUsername(String username) {
        return new UserWS(userDAO.findByUsername(username));
    }

    // Check if user exists
    public Boolean findIfUserExists(String username) {
        System.out.println(username);
        if (userDAO.findByUsername(username) != null){
            System.out.println(userDAO.findByUsername(username).getUsername());

            return true;
        }

        return false;
    }

    // Update user
    public Boolean updateUser(UserWS newUser, String username) {
        User user = userDAO.findByUsername(username);
        if(userDAO.findByUsername(username) != null){
            user.setUsername(newUser.getUsername());
            user.setLastName(newUser.getLastName());
            user.setFirstName(newUser.getFirstName());
            user.setEmail(newUser.getEmail());
            user.setPhone(newUser.getPhone());
            user.setAddress(newUser.getAddress());
            user.setCity(newUser.getCity());
            user.setCountry(newUser.getCountry());
            user.setTIN(newUser.getTIN());
            user.setRole(newUser.getRole());
            user.setIsVerified(newUser.isVerified());
            return userDAO.updateUser(user);
        }

        return false;
    }
}
