package com.carlease.project.user.exceptions;

public class IncorrectPasswordException extends Exception {

    public IncorrectPasswordException(String message) {
        super("Incorrect password for user: " + message);
    }
}