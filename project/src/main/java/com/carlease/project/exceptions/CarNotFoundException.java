package com.carlease.project.exceptions;

public class CarNotFoundException extends Exception {

    public CarNotFoundException(String message) {
        super("Car not found with id: " + message);
    }
}
