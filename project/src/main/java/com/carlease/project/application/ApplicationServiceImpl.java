package com.carlease.project.application;

import com.carlease.project.autosuggestor.Autosuggestor;
import com.carlease.project.autosuggestor.AutosuggestorRepository;
import com.carlease.project.autosuggestor.AutosuggestorServiceImpl;
import com.carlease.project.autosuggestor.CarPrice;
import com.carlease.project.car.Car;
import com.carlease.project.car.CarRepository;
import com.carlease.project.enums.ApplicationStatus;
import com.carlease.project.interestrate.InterestRate;
import com.carlease.project.interestrate.InterestRateDTO;
import com.carlease.project.interestrate.InterestRateMapper;
import com.carlease.project.interestrate.InterestRateService;
import com.carlease.project.user.User;
import com.carlease.project.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final InterestRateService interestRateService;

    private final InterestRateMapper interestRateMapper;

    @Autowired
    private AutosuggestorServiceImpl autosuggestorServiceImpl;
    @Autowired
    private AutosuggestorRepository autosuggestorRepository;

    @Autowired
    private ApplicationMapper applicationMapper;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  CarRepository carRepository, UserRepository userRepository, InterestRateService interestRateService, InterestRateMapper interestRateMapper) {
        this.applicationRepository = applicationRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.interestRateService = interestRateService;
        this.interestRateMapper = interestRateMapper;
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

        InterestRateDTO interestRateDTO = interestRateService.findAll().getFirst();
        InterestRate interestRate = interestRateMapper.toEntity(interestRateDTO);

        CarPrice price = autosuggestorServiceImpl.carPrice(autosuggestorServiceImpl.calculateAverageCarPriceDependingOnYear(application, application.getManufactureDate()));
        if (application.getStatus() == ApplicationStatus.PENDING) {

            Integer calculation = autosuggestorServiceImpl.autosuggest(application, price, interestRate);

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
