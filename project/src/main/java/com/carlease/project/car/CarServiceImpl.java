package com.carlease.project.car;

import com.carlease.project.user.exceptions.CarNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public CarDto updatePrice(long id, CarDto carDto) throws CarNotFoundException {
        Car car = carMapper.carDtoToCar(carDto);

        Car existingCar = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("id"));

        existingCar.setPriceFrom(car.getPriceFrom());
        existingCar.setPriceTo(car.getPriceTo());
        carRepository.save(existingCar);
        return carMapper.carToCarDto(existingCar);
    }

}