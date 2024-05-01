package com.carlease.project.user.exceptions;

public class ApplicationStatusException extends Exception {
    public ApplicationStatusException(String message) {
        super("Application status of : " + message + " is not suitable for this method");
    }
}
