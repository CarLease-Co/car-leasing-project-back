package com.carlease.project.application;

import com.carlease.project.autosuggestor.*;
import com.carlease.project.car.Car;
import com.carlease.project.car.CarRepository;
import com.carlease.project.enums.ApplicationStatus;
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
    private AutosuggestorServiceImpl autosuggestorServiceImpl;
    @Autowired
    private AutosuggestorRepository autosuggestorRepository;

    @Autowired
    private ApplicationMapper applicationMapper;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  CarRepository carRepository, UserRepository userRepository) {
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

        Application application = applicationMapper.applicationFormDtoToApplication(applicationFormDto);

        String carMake = applicationFormDto.getCarMake();
        String carModel = applicationFormDto.getCarModel();

        Car car = carRepository.findByMakeAndModel(carMake, carModel);
        User user = userRepository.findById(applicationFormDto.getUserId()).orElseThrow(
                () -> new RuntimeException("could not create user"));

        application.setCar(car);
        application.setUser(user);

        application.setStatus(applicationFormDto.getStatus());

        return applicationRepository.save(application);
    }

    @Override
    public List<Application> findAllByUserId(long id) {
        return applicationRepository.findApplicationsByUserUserId(id);
    }

    public Integer evaluation(Application application) {

        CarPrice price = autosuggestorServiceImpl.carPrice(autosuggestorServiceImpl.calculateAverageCarPriceDependingOnYear(application, application.getManufactureDate()));
        if (application.getStatus() == ApplicationStatus.PENDING) {

            InterestRate interestRate = new InterestRate(0.05, 0.1, 2010, 2022);

            Integer calculation = autosuggestorServiceImpl.autosuggest(application, price, 50, interestRate);

            Autosuggestor autosuggestor = new Autosuggestor();
            autosuggestor.setApplication(application);
            autosuggestor.setEvaluation(calculation);
            autosuggestorRepository.save(autosuggestor);

            return calculation;
        } else {
            return null;
        }
    }

}
