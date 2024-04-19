package com.carlease.project.user;

import com.carlease.project.user.exceptions.IncorrectPasswordException;
import com.carlease.project.user.exceptions.UserNotFoundException;
import com.carlease.project.user.exceptions.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserServiceImpl userService;
    @Autowired
    private UserController(UserServiceImpl userService){
        this.userService = userService;
    }

    @GetMapping(produces = "application/json")
    ResponseEntity<List<User>> getAllUsers() {
        List<User> list = userService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<User> getById(@PathVariable("id") Long id)
            throws UserNotFoundException {
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/login")
    ResponseEntity<UserSession> login(@RequestParam("username") String username, @RequestParam("password") String password)
            throws UsernameNotFoundException, IncorrectPasswordException {
        UserSession userSession = userService.login(username, password);
        return new ResponseEntity<>(userSession, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}