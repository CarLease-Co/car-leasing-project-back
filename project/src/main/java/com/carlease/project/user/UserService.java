package com.carlease.project.user;

import com.carlease.project.enums.UserRole;
import com.carlease.project.exceptions.IncorrectPasswordException;
import com.carlease.project.exceptions.UserException;
import com.carlease.project.exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

public interface UserService {
    List<User> findAll(long userId, UserRole role) throws UserNotFoundException, UserException;

    User findById(long id) throws UserNotFoundException;

    UserSession login(String username, String password) throws IncorrectPasswordException;

    User createUser(User user, long userId, UserRole role) throws UserNotFoundException, UserException;
}