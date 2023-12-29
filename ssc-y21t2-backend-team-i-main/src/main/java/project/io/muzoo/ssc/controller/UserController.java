package project.io.muzoo.ssc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import project.io.muzoo.ssc.budget.SubBudget;
import project.io.muzoo.ssc.models.*;
import project.io.muzoo.ssc.budget.Budget;
import project.io.muzoo.ssc.log.Log;
import project.io.muzoo.ssc.retirement.Retirement;
import project.io.muzoo.ssc.service.*;
import project.io.muzoo.ssc.user.User;

import java.util.List;

//FOR TESTING PURPOSES
//Backend port: 8090
//Frontend port: 8080

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/getusers")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    /**
     * Register a new user. Recieve the input from
     * POST method and then add the user to the
     * database if pass the verification
     * @param user
     */
    @PostMapping
    public void registerNewUser(@RequestBody UserModel user) {
        userService.addNewUser(user);
    }


    /**
     * Delete the user in the database by id
     * if exists
     * @param userId
     */
    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }


}
