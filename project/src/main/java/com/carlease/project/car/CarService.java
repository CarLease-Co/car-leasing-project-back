package com.carlease.project.car;

import java.util.List;

public interface CarService {
    List<Car> findAll();
    Car findById(long id);
    List<String> findModels(String make);
}