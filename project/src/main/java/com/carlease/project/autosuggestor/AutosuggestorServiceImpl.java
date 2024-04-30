package com.carlease.project.autosuggestor;

import com.carlease.project.application.ApplicationFormDto;
import com.carlease.project.application.ApplicationMapper;
import com.carlease.project.application.ApplicationRepository;
import com.carlease.project.car.Car;
import com.carlease.project.car.CarRepository;
import com.carlease.project.enums.AutosuggestionStatus;
import com.carlease.project.interestrate.InterestRate;
import com.carlease.project.exceptions.AutosuggestorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class AutosuggestorServiceImpl implements AutosuggestorService {
    private static final float PERCENT = 0.1f;
    private static final int UPPER_BOUNDARY = 5;
    private static final int LOWER_BOUNDARY = -5;

    private final AutosuggestorRepository autosuggestorRepository;
    private final CarRepository carRepository;
    private final AutosuggestorMapper autosuggestorMapper;
    private final ApplicationMapper applicationMapper;

    @Autowired
    public AutosuggestorServiceImpl(AutosuggestorRepository autosuggestorRepository, CarRepository carRepository, ApplicationRepository applicationRepository, AutosuggestorMapper autosuggestorMapper, ApplicationMapper applicationMapper) {
        this.autosuggestorRepository = autosuggestorRepository;
        this.carRepository = carRepository;
        this.autosuggestorMapper = autosuggestorMapper;
        this.applicationMapper = applicationMapper;
    }

    @Override
    public AutosuggestorDto findById(long id) throws AutosuggestorNotFoundException {
        Autosuggestor autosuggestor = autosuggestorRepository.findById(id).orElseThrow(() -> new AutosuggestorNotFoundException("id"));
        return autosuggestorMapper.toDto(autosuggestor);
    }

    @Override
    public double calculateInterestRate(ApplicationFormDto applicationDto, InterestRate interestRate) {

        int carYear = applicationDto.getManufactureDate();
        double slope = (interestRate.getInterestFrom() - interestRate.getInterestTo()) / (interestRate.getYearTo() - interestRate.getYearFrom());
        double calculatedRate = interestRate.getInterestTo() + slope * (carYear - interestRate.getYearFrom());
        calculatedRate = Math.max(Math.min(calculatedRate, interestRate.getInterestTo()), interestRate.getInterestFrom());

        return calculatedRate;
    }

    public List<AutosuggestorDto> findAll() {
        List<Autosuggestor> autosuggestors = autosuggestorRepository.findAll();
        return autosuggestors.stream()
                .map(autosuggestorMapper::toDto)
                .toList();
    }

    @Override
    public BigDecimal calculateTotalLoanPrice(ApplicationFormDto applicationDto, double interestRate) {

        int loanDuration = applicationDto.getLoanDuration();
        BigDecimal loanAmount = applicationDto.getLoanAmount();
        Double loanInYears = (double) loanDuration / 12;
        double interestRateValue = interestRate + 1;
        BigDecimal totalInterestValue = BigDecimal.valueOf(Math.pow(interestRateValue, loanInYears));
        BigDecimal totalPrice = loanAmount.multiply(totalInterestValue).setScale(2, BigDecimal.ROUND_HALF_UP);
        return totalPrice;
    }

    public BigDecimal calculateAverageCarPriceDependingOnYear(ApplicationFormDto applicationDto) {
        BigDecimal carPrice;
        int currentYear = LocalDate.now().getYear();

        Car car = carRepository.findByMakeAndModel(applicationDto.getCarMake(), applicationDto.getCarModel());
        BigDecimal maxPrice = car.getPriceTo();
        BigDecimal carPriceUpToTenYears = car.getPriceFrom();
        int carYear = applicationDto.getManufactureDate();

        BigDecimal priceChangePerYearUpToTenYears = maxPrice.subtract(carPriceUpToTenYears).divide(BigDecimal.TEN);

        BigDecimal difference = BigDecimal.ZERO;
        if (!carPriceUpToTenYears.equals(BigDecimal.ZERO)) {
            difference = maxPrice.divide(carPriceUpToTenYears, 2, BigDecimal.ROUND_HALF_UP);
        }

        BigDecimal carPriceUpToTwentyYears = carPriceUpToTenYears.divide(difference, 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal carPriceUpToThirtyYears = carPriceUpToTwentyYears.divide(difference, 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal priceChangePerYearUpToTwentyYears = carPriceUpToTenYears.subtract(carPriceUpToTwentyYears).divide(BigDecimal.TEN);

        BigDecimal priceChangePerYearUpToThirtyYears = carPriceUpToTwentyYears.subtract(carPriceUpToThirtyYears).divide(BigDecimal.TEN);

        int carOld = currentYear - carYear;

        if (carYear > currentYear - 10) {
            carPrice = maxPrice.subtract(BigDecimal.valueOf(carOld).multiply(priceChangePerYearUpToTenYears));

        } else if (carYear > currentYear - 20) {
            carPrice = carPriceUpToTenYears.subtract(BigDecimal.valueOf(carOld - 10).multiply(priceChangePerYearUpToTwentyYears));

        } else {
            carPrice = carPriceUpToTwentyYears.subtract(BigDecimal.valueOf(carOld - 20).multiply(priceChangePerYearUpToThirtyYears));
        }
        return carPrice;
    }

    public CarPrice calculateAvgCarPriceRange(BigDecimal price) {
        BigDecimal value = price.multiply(BigDecimal.valueOf(PERCENT));
        return new CarPrice(price, price.subtract(value), price.add(value));
    }

    public Integer loanAmountEvaluation(CarPrice price, BigDecimal loanAmount) {
        BigDecimal evaluationRange = price.getAvgMax().subtract(price.getAvgPrice());
        if (loanAmount.compareTo(price.getAvgMax()) == 1) {
            BigDecimal loanEvaluationRange = loanAmount.subtract(price.getAvgPrice());
            return -loanEvaluationRange.divide(evaluationRange, 2, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.ONE).intValue();
        } else if (price.getAvgMin().compareTo(loanAmount) == 1) {
            BigDecimal loanEvaluationRange = price.getAvgPrice().subtract(loanAmount);
            return loanEvaluationRange.divide(evaluationRange, 2, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.ONE).intValue();
        }
        return 0;
    }

    public Integer paymentEvaluation(ApplicationFormDto applicationDto, double interestRate, double rate) {
        BigDecimal monthlyPayment = calculateTotalLoanPrice(applicationDto, interestRate).divide(BigDecimal.valueOf(applicationDto.getLoanDuration()), 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal maxPossibleObligations = (applicationDto.getMonthlyIncome().subtract(applicationDto.getFinancialObligations()).multiply(BigDecimal.valueOf(rate)));

        if (maxPossibleObligations.compareTo(monthlyPayment) == 1) {
            return BigDecimal.ONE.subtract(monthlyPayment.divide(maxPossibleObligations, 2, BigDecimal.ROUND_HALF_UP)).multiply(BigDecimal.TEN).intValue();
        } else if (monthlyPayment.compareTo(maxPossibleObligations) == 1) {
            return -monthlyPayment.divide(maxPossibleObligations, 2, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.ONE).multiply(BigDecimal.TEN).intValue();
        }
        return 0;
    }

    @Override
    public Integer autosuggest(ApplicationFormDto applicationDto, CarPrice price, InterestRate interestRate) {
        InterestRate calculatedInterestRate = InterestRate.builder()
                .rate(interestRate.getRate())
                .interestFrom(interestRate.getInterestFrom())
                .interestTo(interestRate.getInterestTo())
                .yearFrom(interestRate.getYearFrom())
                .yearTo(interestRate.getYearTo())
                .build();

        int pointsForLoan = loanAmountEvaluation(price, applicationDto.getLoanAmount());
        int pointsForPayment = paymentEvaluation(applicationDto, calculateInterestRate(applicationDto, calculatedInterestRate), calculatedInterestRate.getRate());
        int evaluation = pointsForLoan + pointsForPayment;
        save(applicationDto, evaluation);
        return evaluation;
    }

    private void save(ApplicationFormDto applicationDto, Integer evaluation) {
        Autosuggestor autosuggestion = new Autosuggestor();
        autosuggestion.setApplication(applicationMapper.toEntity(applicationDto));
        autosuggestion.setEvaluation(evaluation);
        if (evaluation >= UPPER_BOUNDARY) {
            autosuggestion.setEvalStatus(AutosuggestionStatus.GOOD);
        } else if (evaluation >= LOWER_BOUNDARY) {
            autosuggestion.setEvalStatus(AutosuggestionStatus.MAYBE);
        } else {
            autosuggestion.setEvalStatus(AutosuggestionStatus.BAD);
        }
        autosuggestorRepository.save(autosuggestion);
    }
}