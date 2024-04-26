package com.carlease.project.user.exceptions;

public class ApplicationNotFoundException extends Exception {

    public ApplicationNotFoundException(String message) {
        super("Application not found with id: " + message);
    }
}
