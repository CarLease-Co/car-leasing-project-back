package com.carlease.project.car;

import com.carlease.project.enums.UserRole;
import com.carlease.project.exceptions.CarNotFoundException;
import com.carlease.project.exceptions.UserException;
import com.carlease.project.exceptions.UserNotFoundException;

import java.util.List;

public interface CarService {
    List<CarDto> findAll();

    CarDto findById(long id) throws CarNotFoundException;

    List<String> findModels(String make);

    CarDto updatePrice(CarDto carDto, long userId, UserRole role) throws CarNotFoundException, UserNotFoundException, UserException;
}