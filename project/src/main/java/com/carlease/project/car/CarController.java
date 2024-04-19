package com.carlease.project.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    ResponseEntity<List<Car>> getCars() {
        List<Car> list = carServiceImpl.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Car> getCar(@PathVariable("id") long id) {

        Car car = carServiceImpl.findById(id);
        return new ResponseEntity<>(car, HttpStatus.OK);
    }

    @GetMapping("/{make}/models")
    @ResponseBody
    ResponseEntity<List<String>> getModels(@PathVariable("make") String make){
        List <String> models = carServiceImpl.findModels(make);
        return new ResponseEntity<>(models, HttpStatus.OK);
    }
}