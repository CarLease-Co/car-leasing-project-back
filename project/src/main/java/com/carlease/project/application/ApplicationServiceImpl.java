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
import java.util.Objects;
import java.util.Optional;

import static com.carlease.project.user.UserServiceImpl.validateUserRole;

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
    public ApplicationFormDto create(ApplicationFormDto applicationFormDto, long userId, UserRole role) throws UserNotFoundException, UserException {
        validateUserRole(userRepository, userId, role);
        Application application = applicationMapper.toEntity(applicationFormDto);
        Car car = carRepository.findByMakeAndModel(applicationFormDto.getCarMake(), applicationFormDto.getCarModel());
        User user = userRepository.findById(applicationFormDto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(applicationFormDto.getUserId()));

        if (!(role.equals(UserRole.APPLICANT))) {
            throw new UserException("Don't have permission to create application");
        }

        application.setCar(car);
        application.setUser(user);

        application.setStatus(applicationFormDto.getStatus());

        Application savedApplication = applicationRepository.save(application);
        return applicationMapper.toDto(savedApplication);
    }

    @Override
    public ApplicationFormDto updateStatus(long applicationId, long userId, ApplicationStatus status, UserRole role) throws ApplicationNotFoundException, UserException, UserNotFoundException {
        validateUserRole(userRepository,userId, role);
        Application application = applicationRepository.findById(applicationId).orElseThrow(() -> new ApplicationNotFoundException("id"));

        switch (role) {
            case APPLICANT:
                if (!isValidApplicantStatus(application.getStatus() ,status)) {
                    throw new UserException("Applicant cannot update status to " + status);
                }
                break;
            case REVIEWER:
                if (!isValidReviewerStatus(application.getStatus(), status)) {
                    throw new UserException("Reviewer cannot update status to " + status);
                }
                break;
            case APPROVER:
                if (!isValidApproverStatus(application.getStatus(), status)){
                    throw new UserException("Approver cannot update status to " + status);
                }
                break;
            default:
                throw new UserException("Unsupported user role: " + role);
        }

        application.setStatus(status);
        Application savedApplication = applicationRepository.save(application);
        return applicationMapper.toDto(savedApplication);
    }

    private boolean isValidApplicantStatus(ApplicationStatus currentStatus ,ApplicationStatus newStatus) {
        return Objects.equals(currentStatus, ApplicationStatus.DRAFT) && Objects.equals(newStatus, ApplicationStatus.PENDING);
    }

    private boolean isValidReviewerStatus(ApplicationStatus currentStatus, ApplicationStatus newStatus) {
        return Objects.equals(currentStatus, ApplicationStatus.PENDING) && ((Objects.equals(newStatus, ApplicationStatus.REVIEW_APPROVED) || (Objects.equals(newStatus, ApplicationStatus.REVIEW_DECLINED))));
    }

    private boolean isValidApproverStatus(ApplicationStatus currentStatus, ApplicationStatus newStatus) {
        return ((Objects.equals(currentStatus, ApplicationStatus.REVIEW_DECLINED)) || (Objects.equals(currentStatus, ApplicationStatus.REVIEW_APPROVED))) && ((Objects.equals(newStatus, ApplicationStatus.APPROVED)) || (Objects.equals(newStatus, ApplicationStatus.DECLINED)) || (Objects.equals(newStatus, ApplicationStatus.PENDING)));
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
        validateUserRole(userRepository, id, role);

        return switch (role) {
            case APPLICANT -> findAllByUserId(id);
            case REVIEWER -> findAllByStatus(ApplicationStatus.PENDING);
            case APPROVER ->
                    findAllByStatuses(List.of(ApplicationStatus.REVIEW_APPROVED, ApplicationStatus.REVIEW_DECLINED));
            default -> null;
        };
    }



    public void evaluation(ApplicationFormDto applicationDto) throws AutosuggestorNotFoundException {

        InterestRateDTO interestRateDTO = interestRateService.findAll().getFirst();
        InterestRate interestRate = interestRateMapper.toEntity(interestRateDTO);

        CarPrice price = autosuggestorServiceImpl.calculateAvgCarPriceRange(autosuggestorServiceImpl.calculateAverageCarPriceDependingOnYear(applicationDto));
        if (ApplicationStatus.PENDING.equals(applicationDto.getStatus())) {
            AutosuggestorDto existingAutosuggestion = findAutosuggestorByApplicationId(applicationDto.getId());
            if (existingAutosuggestion == null) {
                autosuggestorServiceImpl.autosuggest(applicationDto, price, interestRate);
            }
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
        validateUserRole(userRepository, userId, role);

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
        validateUserRole(userRepository, userId, role);

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
