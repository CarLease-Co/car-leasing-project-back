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
import com.carlease.project.user.exceptions.ApplicationNotFoundException;
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
    public List<ApplicationFormDto> findAll() {
        List<Application> applications = applicationRepository.findAll();
        return applications.stream()
                .map(applicationMapper::toDto)
                .toList();
    }

    @Override
    public ApplicationFormDto findById(long id) throws ApplicationNotFoundException {
        Application application = applicationRepository.findById(id).orElseThrow(() -> new ApplicationNotFoundException("id"));
        return applicationMapper.toDto(application);
    }

    @Override
    public ApplicationFormDto create(ApplicationFormDto applicationFormDto) {
        Application application = applicationMapper.toEntity(applicationFormDto);

        Car car = carRepository.findByMakeAndModel(applicationFormDto.getCarMake(), applicationFormDto.getCarModel());

        User user = userRepository.findById(applicationFormDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Could not find user"));

        application.setCar(car);
        application.setUser(user);

        application.setStatus(applicationFormDto.getStatus());

        Application savedApplication = applicationRepository.save(application);
        return applicationMapper.toDto(savedApplication);
    }

      @Override
    public List<ApplicationFormDto> findAllByUserId(long id) {
        List<Application> applications = applicationRepository.findApplicationsByUserUserId(id);
        return applications.stream()
                .map(applicationMapper::toDto)
                .toList();
    }

    public void evaluation(ApplicationFormDto applicationDto) {

        InterestRateDTO interestRateDTO = interestRateService.findAll().getFirst();
        InterestRate interestRate = interestRateMapper.toEntity(interestRateDTO);

        CarPrice price = autosuggestorServiceImpl.carPrice(autosuggestorServiceImpl.calculateAverageCarPriceDependingOnYear(applicationDto, applicationDto.getManufactureDate()));
        if (applicationDto.getStatus() == ApplicationStatus.PENDING) {

            Integer calculation = autosuggestorServiceImpl.autosuggest(applicationDto, price, interestRate);

            Autosuggestor autosuggestor = new Autosuggestor();
            autosuggestor.setApplication(applicationMapper.toEntity(applicationDto));
            autosuggestor.setEvaluation(calculation);
            autosuggestorRepository.save(autosuggestor);

        } else {
        }
    }

}
