package com.carlease.project.car;

import com.carlease.project.application.Application;
import com.carlease.project.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/cars")
public class CarController {
    private CarServiceImpl carServiceImpl;

    @Autowired
    private CarController(CarServiceImpl carServiceImpl){
        this.carServiceImpl = carServiceImpl;
    }

    @GetMapping(produces = "application/json")
    ResponseEntity<List<Car>> getAllCars() {
        List<Car> list = carServiceImpl.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Car> getCarById(@PathVariable("id") long id) {

        Car car = carServiceImpl.findById(id);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

}
