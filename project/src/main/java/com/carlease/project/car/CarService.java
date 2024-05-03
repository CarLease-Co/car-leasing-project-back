package com.carlease.project.car;

import com.carlease.project.exceptions.CarNotFoundException;

import java.util.List;

public interface CarService {
    List<CarDto> findAll();

    CarDto findById(long id) throws CarNotFoundException;

    List<String> findModels(String make);

    CarDto updatePrice(CarDto carDto) throws CarNotFoundException;
}