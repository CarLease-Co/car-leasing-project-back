package com.carlease.project.user.exceptions;

public class CarNotFoundException extends Exception {

    public CarNotFoundException(String message) {
        super("Car not found with id: " + message);
    }
}
