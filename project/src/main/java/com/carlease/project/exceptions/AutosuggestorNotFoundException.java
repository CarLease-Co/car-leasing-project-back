package com.carlease.project.exceptions;

public class AutosuggestorNotFoundException extends Exception {

    public AutosuggestorNotFoundException(String message) {
        super("Autosuggestion not found with id: " + message);
    }
}