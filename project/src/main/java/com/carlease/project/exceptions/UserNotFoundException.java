package com.carlease.project.exceptions;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(long id) {

        super("Can't find user with " + id);
    }
}

