package com.carlease.project.application;

import com.carlease.project.autosuggestor.*;
import com.carlease.project.car.Car;
import com.carlease.project.car.CarRepository;
import com.carlease.project.enums.ApplicationStatus;
import com.carlease.project.enums.UserRole;
import com.carlease.project.exceptions.UserException;
import com.carlease.project.interestrate.InterestRate;
import com.carlease.project.interestrate.InterestRateDTO;
import com.carlease.project.interestrate.InterestRateMapper;
import com.carlease.project.interestrate.InterestRateService;
import com.carlease.project.user.User;
import com.carlease.project.user.UserRepository;
import com.carlease.project.exceptions.ApplicationNotFoundException;
import com.carlease.project.exceptions.AutosuggestorNotFoundException;
import com.carlease.project.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final InterestRateService interestRateService;
    private final InterestRateMapper interestRateMapper;
    private final AutosuggestorServiceImpl autosuggestorServiceImpl;
    private final AutosuggestorRepository autosuggestorRepository;
    private final ApplicationMapper applicationMapper;
    private final AutosuggestorMapper autosuggestorMapper;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  CarRepository carRepository, UserRepository userRepository, InterestRateService interestRateService, InterestRateMapper interestRateMapper, AutosuggestorServiceImpl autosuggestorServiceImpl, AutosuggestorRepository autosuggestorRepository, ApplicationMapper applicationMapper, AutosuggestorMapper autosuggestorMapper) {
        this.applicationRepository = applicationRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.interestRateService = interestRateService;
        this.interestRateMapper = interestRateMapper;
        this.autosuggestorServiceImpl = autosuggestorServiceImpl;
        this.autosuggestorRepository = autosuggestorRepository;
        this.applicationMapper = applicationMapper;
        this.autosuggestorMapper = autosuggestorMapper;
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
    public ApplicationFormDto create(ApplicationFormDto applicationFormDto) throws UserNotFoundException {
        Application application = applicationMapper.toEntity(applicationFormDto);

        Car car = carRepository.findByMakeAndModel(applicationFormDto.getCarMake(), applicationFormDto.getCarModel());

        User user = userRepository.findById(applicationFormDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(applicationFormDto.getUserId()));

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

    @Override
    public List<ApplicationFormDto> findAllByStatus(ApplicationStatus status) {
        List<Application> applications = applicationRepository.findByStatus(status);
        return applications.stream()
                .map(applicationMapper::toDto)
                .toList();
    }

    @Override
    public List<ApplicationFormDto> findAllByStatuses(List<ApplicationStatus> statuses) {
        List<Application> applications = applicationRepository.findByStatusIn(statuses);
        return applications.stream()
                .map(applicationMapper::toDto)
                .toList();
    }

    @Override
    public List<ApplicationFormDto> getApplicationsByUser(long id, UserRole role) throws UserException {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.get();

        if (!user.getRole().equals(role)) {
            throw new UserException("User role does not match the provided role");
        }

        switch (role) {
            case APPLICANT:
                return findAllByUserId(id);
            case REVIEWER:
                return findAllByStatus(ApplicationStatus.PENDING);
            case APPROVER:
                return findAllByStatuses(List.of(ApplicationStatus.REVIEW_APPROVED, ApplicationStatus.REVIEW_DECLINED));
            default:
                return null;
        }
    }

    public void evaluation(ApplicationFormDto applicationDto) {

        InterestRateDTO interestRateDTO = interestRateService.findAll().getFirst();
        InterestRate interestRate = interestRateMapper.toEntity(interestRateDTO);

        CarPrice price = autosuggestorServiceImpl.calculateAvgCarPriceRange(autosuggestorServiceImpl.calculateAverageCarPriceDependingOnYear(applicationDto));
        if (ApplicationStatus.PENDING.equals(applicationDto.getStatus())) {
            autosuggestorServiceImpl.autosuggest(applicationDto, price, interestRate);
        }
    }

    @Override
    public AutosuggestorDto findAutosuggestorByApplicationId(long id) throws ApplicationNotFoundException {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isEmpty()) {
            throw new ApplicationNotFoundException("Application not found with ID: " + id);
        }
        Autosuggestor autosuggestor = autosuggestorRepository.findByApplicationId(id);
        return autosuggestorMapper.toDto(autosuggestor);
    }

    @Override
    public boolean deleteById(long id) {
        Optional<Application> optionalApplication = applicationRepository.findById(id);

        if (optionalApplication.isPresent()) {
            Application application = optionalApplication.get();

            if (ApplicationStatus.DRAFT.equals(application.getStatus())) {
                applicationRepository.deleteById(id);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
