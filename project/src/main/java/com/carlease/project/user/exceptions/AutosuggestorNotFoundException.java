package com.carlease.project.user.exceptions;

public class AutosuggestorNotFoundException extends Exception {

    public AutosuggestorNotFoundException(String message) {
        super("Autosuggestion not found with id: " + message);
    }
}