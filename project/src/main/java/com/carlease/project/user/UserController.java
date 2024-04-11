package com.carlease.project.user;

import com.carlease.project.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private UserServiceImpl userService;
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
        return new ResponseEntity<User>(user, HttpStatus.OK);

    }
}
