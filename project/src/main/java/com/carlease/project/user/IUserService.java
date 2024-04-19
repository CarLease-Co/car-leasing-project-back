package com.carlease.project.user;

import com.carlease.project.user.exceptions.IncorrectPasswordException;
import com.carlease.project.user.exceptions.UserNotFoundException;
import com.carlease.project.user.exceptions.UsernameNotFoundException;

import java.util.List;

public interface IUserService {
    List<User> findAll();
    User findById(long id) throws UserNotFoundException;
    UserSession login(String username, String password) throws UsernameNotFoundException, IncorrectPasswordException;
    User createUser(User user);
}