package com.carlease.project.application;

import com.carlease.project.autosuggestor.*;
import com.carlease.project.car.Car;
import com.carlease.project.car.CarRepository;
import com.carlease.project.enums.ApplicationStatus;
import com.carlease.project.enums.UserRole;
import com.carlease.project.exceptions.*;
import com.carlease.project.interestrate.InterestRate;
import com.carlease.project.interestrate.InterestRateDTO;
import com.carlease.project.interestrate.InterestRateMapper;
import com.carlease.project.interestrate.InterestRateService;
import com.carlease.project.user.User;
import com.carlease.project.user.UserRepository;
import com.carlease.project.user.UserServiceImpl;
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
    public ApplicationFormDto updateStatus(long id, ApplicationStatus status) throws ApplicationNotFoundException {
        Application application = applicationRepository.findById(id).orElseThrow(() -> new ApplicationNotFoundException("id"));
        application.setStatus(status);
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
    public List<ApplicationFormDto> getApplicationsByUser(long id, UserRole role) throws UserException, UserNotFoundException {
        UserServiceImpl.validateUserRole(userRepository, id, role);

        return switch (role) {
            case APPLICANT -> findAllByUserId(id);
            case REVIEWER -> findAllByStatus(ApplicationStatus.PENDING);
            case APPROVER ->
                    findAllByStatuses(List.of(ApplicationStatus.REVIEW_APPROVED, ApplicationStatus.REVIEW_DECLINED));
            default -> null;
        };
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
    public AutosuggestorDto findAutosuggestorByApplicationId(long id) throws AutosuggestorNotFoundException {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isEmpty()) {
            throw new AutosuggestorNotFoundException("Application not found with ID: " + id);
        }
        Autosuggestor autosuggestor = autosuggestorRepository.findByApplicationId(id);
        return autosuggestorMapper.toDto(autosuggestor);
    }

    @Override
    public boolean deleteById(long applicationId, long userId, UserRole role) throws UserException, UserNotFoundException {
        UserServiceImpl.validateUserRole(userRepository, userId, role);

        Optional<Application> optionalApplication = applicationRepository.findById(applicationId);

        if (optionalApplication.isPresent()) {
            Application application = optionalApplication.get();
            if (application.getUser().getUserId() != userId) {
                return false;
            }
            if (UserRole.APPLICANT.equals(application.getUser().getRole()) && ApplicationStatus.DRAFT.equals(application.getStatus())) {
                applicationRepository.deleteById(applicationId);
                return true;
            }
        }
        return false;
    }

    @Override
    public ApplicationFormDto update(long applicationId, ApplicationFormDto applicationFormDto, long userId, UserRole role) throws ApplicationNotFoundException, ApplicationNotDraftException, UserException, UserNotFoundException {
        UserServiceImpl.validateUserRole(userRepository, userId, role);

        Optional<Application> optionalApplication = applicationRepository.findById(applicationId);

        if (optionalApplication.isPresent()) {
            Application existingApplication = optionalApplication.get();

            if (existingApplication.getUser().getUserId() != userId) {
                throw new UserException("User id does not match application id");
            }
            if (!UserRole.APPLICANT.equals(existingApplication.getUser().getRole())) {
                throw new UserException("User role does not match the provided role");
            }
            if (ApplicationStatus.DRAFT.equals(existingApplication.getStatus())) {

                existingApplication.setMonthlyIncome(applicationFormDto.getMonthlyIncome());
                existingApplication.setFinancialObligations(applicationFormDto.getFinancialObligations());
                existingApplication.setManufactureDate(applicationFormDto.getManufactureDate());
                existingApplication.setTextExplanation(applicationFormDto.getTextExplanation());
                existingApplication.setLoanDuration(applicationFormDto.getLoanDuration());
                existingApplication.setLoanAmount(applicationFormDto.getLoanAmount());
                existingApplication.setStatus(applicationFormDto.getStatus());

                Car selectedCar = carRepository.findByMakeAndModel(applicationFormDto.getCarMake(), applicationFormDto.getCarModel());
                existingApplication.setCar(selectedCar);
                applicationRepository.save(existingApplication);
                return applicationMapper.toDto(existingApplication);
            } else {
                throw new ApplicationNotDraftException("Cannot update application as status is not DRAFT");
            }
        } else {
            throw new ApplicationNotFoundException("Application not found with ID: " + applicationFormDto.getId());
        }
    }

}
