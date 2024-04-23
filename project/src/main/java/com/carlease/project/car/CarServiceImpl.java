package com.carlease.project.car;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> findAll() {
        return carRepository.findAll();
    }

    @Override
    public Car findById(long id) {
        return carRepository.findById(id).orElseThrow();
    }

    @Override
    public List<String> findModels(String make){
        List<Car> cars = carRepository.findByMake(make);
        return cars.stream().map(Car::getModel).distinct().toList();
    }
}