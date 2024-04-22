package com.carlease.project.user;

import com.carlease.project.user.exceptions.IncorrectPasswordException;
import com.carlease.project.user.exceptions.UserNotFoundException;

import java.util.List;

public interface IUserService {
    List<User> findAll();

    User findById(long id) throws UserNotFoundException;

    UserSession login(String username, String password) throws IncorrectPasswordException;

    User createUser(User user);
}