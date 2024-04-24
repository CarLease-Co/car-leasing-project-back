package com.carlease.project.car;

import com.carlease.project.user.exceptions.CarNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements ICarService {

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
    public List<String> findModels(String make) {
        List<Car> cars = carRepository.findByMake(make);
        return cars.stream().map(Car::getModel).distinct().toList();
    }

    @Override
    public Car updatePrice(long id, Car car) throws CarNotFoundException {
        Car existingCar = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("id"));

        existingCar.setPriceFrom(car.getPriceFrom());
        existingCar.setPriceTo(car.getPriceTo());
        return carRepository.save(existingCar);
    }

}