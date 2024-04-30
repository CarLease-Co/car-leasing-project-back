package com.carlease.project.car;

import com.carlease.project.exceptions.CarNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    @Autowired
    private CarMapper carMapper;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<CarDto> findAll() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(carMapper::carToCarDto)
                .toList();
    }

    @Override
    public CarDto findById(long id) throws CarNotFoundException {
        Car car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("id"));
        return carMapper.carToCarDto(car);
    }

    @Override
    public List<String> findModels(String make) {
        List<Car> cars = carRepository.findByMake(make);
        return cars.stream().map(Car::getModel).distinct().toList();
    }

    @Override
    public CarDto updatePrice(CarDto carDto) throws CarNotFoundException {
        String make = carDto.getMake();
        String model = carDto.getModel();
        Car existingCar = carRepository.findByMakeAndModel(make, model);

        existingCar.setPriceFrom(carDto.getPriceFrom());
        existingCar.setPriceTo(carDto.getPriceTo());
        carRepository.save(existingCar);
        return carMapper.carToCarDto(existingCar);
    }

}