package com.carlease.project.application;

import com.carlease.project.car.Car;
import com.carlease.project.car.CarRepository;
import com.carlease.project.user.User;
import com.carlease.project.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CarRepository carRepository;

    private final UserRepository userRepository;

    @Autowired
    private ApplicationMapper applicationMapper;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  CarRepository carRepository, UserRepository userRepository
                                  ) {
        this.applicationRepository = applicationRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;

    }

    @Override
    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    @Override
    public Application findById(long id) {
        return applicationRepository.findById(id).orElseThrow();
    }

    @Override
    public Application create(ApplicationFormDto applicationFormDto) {

        String carMake = applicationFormDto.getCarMake();
        String carModel = applicationFormDto.getCarModel();

        Car car = carRepository.findByMakeAndModel(carMake, carModel);
        Optional<User> user = userRepository.findById(applicationFormDto.getUserId());

        Application application = applicationMapper.applicationFormDtoToApplication(applicationFormDto);
        application.setCar(car);


        return applicationRepository.save(application);
    }

    @Override
    public List<Application> findAllByUserId(long id) {return applicationRepository.findApplicationsByUserId(id);}

}
