package com.carlease.project.exceptions;

public class UsernameNotFoundException extends Exception {

    public UsernameNotFoundException(String message) {
        super("User not found with username: " + message);
    }
}