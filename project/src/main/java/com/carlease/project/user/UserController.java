package com.carlease.project.user;

import com.carlease.project.enums.UserRole;
import com.carlease.project.exceptions.IncorrectPasswordException;
import com.carlease.project.exceptions.UserException;
import com.carlease.project.exceptions.UserNotFoundException;
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
    private UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping(produces = "application/json")
    ResponseEntity<List<User>> getUsers() {
        List<User> list = userService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<User> getUser(@PathVariable("id") Long id)
            throws UserNotFoundException {
        User user = userService.findById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody UserCredentials user) {
        try {
            UserSession userSession = userService.login(user.getUsername(), user.getPassword());
            return new ResponseEntity<>(userSession, HttpStatus.OK);
        } catch (IncorrectPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping
    ResponseEntity<User> createUser(@RequestBody User user, @RequestHeader("userId") long userId, @RequestHeader("role") UserRole role) throws UserNotFoundException, UserException {
        User newUser = userService.createUser(user, userId, role);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }
}