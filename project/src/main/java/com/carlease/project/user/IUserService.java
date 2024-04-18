package com.carlease.project.user;

import com.carlease.project.user.exceptions.UserNotFoundException;

import java.util.List;

public interface IUserService {
    List<User> findAll();
    User findById(long id) throws UserNotFoundException;
    User createUser(User user);
}
