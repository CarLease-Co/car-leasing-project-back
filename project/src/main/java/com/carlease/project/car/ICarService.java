package com.carlease.project.car;
import com.carlease.project.user.User;
import com.carlease.project.user.exceptions.CarNotFoundException;

import java.util.List;

public interface ICarService {
    List<Car> findAll();
    Car findById(long id);
    List<String> findModels(String make);
    Car updatePrice(long id, Car car) throws CarNotFoundException;
}