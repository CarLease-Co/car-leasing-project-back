package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AutosuggestorServiceImpl implements AutosuggestorService {
    private static final float PERCENT = 0.1f;
    private final AutosuggestorRepository autosuggestorRepository;

    @Autowired
    private AutosuggestorMapper autosuggestorMapper;

    public AutosuggestorServiceImpl(AutosuggestorRepository autosuggestorRepository) {
        this.autosuggestorRepository = autosuggestorRepository;
    }

    @Override
    public Autosuggestor findById(long id) {
        return autosuggestorRepository.findById(id).orElseThrow();
    }

    @Override
    public double calculateInterestRate(Application application, double interestFrom, double interestTo, int yearFrom, int yearTo) {

        int carYear = application.getManufactureDate();
        double slope = (interestFrom - interestTo) / (yearTo - yearFrom);
        double interestRate = interestTo + slope * (carYear - yearFrom);
        interestRate = Math.max(Math.min(interestRate, interestTo), interestFrom);

        return interestRate;
    }

    public List<Autosuggestor> findAll() {
        return autosuggestorRepository.findAll();
    }

    @Override
    public BigDecimal calculateTotalLoanPrice(Application application, double interestRate) {

        int loanDuration = application.getLoanDuration();
        BigDecimal loanAmount = application.getLoanAmount();
        double interestRateValue = interestRate / 100 + 1;
        BigDecimal totalInterestValue = BigDecimal.valueOf(Math.pow(interestRateValue, loanDuration));
        return loanAmount.multiply(totalInterestValue);
    }

    public BigDecimal calculateAverageCarPriceDependingOnYear(Application application, int currentYear) {
        BigDecimal carPrice;

        BigDecimal maxPrice = application.getCar().getPriceTo();
        BigDecimal carPriceUpToTenYears = application.getCar().getPriceFrom();
        int carYear = application.getManufactureDate();

        BigDecimal priceChangePerYearUpToTenYears = maxPrice.subtract(carPriceUpToTenYears).divide(BigDecimal.TEN);

        BigDecimal difference = BigDecimal.ZERO;
        if (!carPriceUpToTenYears.equals(BigDecimal.ZERO)) {
            difference = maxPrice.divide(carPriceUpToTenYears, 2, BigDecimal.ROUND_HALF_UP);
        }

        BigDecimal carPriceUpToTwentyYears = carPriceUpToTenYears.divide(difference, 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal carPriceUpToThirtyYears = carPriceUpToTwentyYears.divide(difference, 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal priceChangePerYearUpToTwentyYears = carPriceUpToTenYears.subtract(carPriceUpToTwentyYears).divide(BigDecimal.TEN);

        BigDecimal priceChangePerYearUpToThirtyYears = carPriceUpToTwentyYears.subtract(carPriceUpToThirtyYears).divide(BigDecimal.TEN);

        int carOld = currentYear = carYear;

        if (carYear > currentYear - 10) {
            carPrice = maxPrice.subtract(BigDecimal.valueOf(carOld).multiply(priceChangePerYearUpToTenYears));

        } else if (carYear > currentYear - 20) {
            carPrice = carPriceUpToTenYears.subtract(BigDecimal.valueOf(carOld - 10).multiply(priceChangePerYearUpToTwentyYears));

        } else {
            carPrice = carPriceUpToTwentyYears.subtract(BigDecimal.valueOf(carOld - 20).multiply(priceChangePerYearUpToThirtyYears));
        }
        return carPrice;
    }

    public CarPrice carPrice(BigDecimal price) {
        BigDecimal value = price.multiply(BigDecimal.valueOf(PERCENT));
        return new CarPrice(price, price.subtract(value), price.add(value));
    }

    public Integer loanAmountEvaluation(CarPrice price, BigDecimal loanAmount) {
        if (loanAmount.compareTo(price.getAvgMax()) == 1) {
            BigDecimal evaluationRange = price.getAvgMax().subtract(price.getAvgPrice());
            BigDecimal loanEvaluationRange = loanAmount.subtract(price.getAvgPrice());
            return -loanEvaluationRange.divide(evaluationRange, 2, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.ONE).intValue();
        } else if (price.getAvgMin().compareTo(loanAmount) == 1) {
            BigDecimal evaluationRange = price.getAvgPrice().subtract(price.getAvgMin());
            BigDecimal loanEvaluationRange = price.getAvgPrice().subtract(loanAmount);
            return loanEvaluationRange.divide(evaluationRange, 2, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.ONE).intValue();
        }
        return 0;
    }

    public Integer paymentEvaluation(Application application, double interestRate, double rate) {
        BigDecimal monthlyPayment = calculateTotalLoanPrice(application, interestRate).divide(BigDecimal.valueOf(application.getLoanDuration()), 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal maxPossibleObligations = application.getMonthlyIncome().subtract(application.getFinancialObligations().multiply(BigDecimal.valueOf(rate)));

        if (maxPossibleObligations.compareTo(monthlyPayment) == 1) {
            return BigDecimal.ONE.subtract(monthlyPayment.divide(maxPossibleObligations, 2, BigDecimal.ROUND_HALF_UP)).multiply(BigDecimal.TEN).intValue();
        } else if (monthlyPayment.compareTo(maxPossibleObligations) == 1) {
            return -monthlyPayment.divide(maxPossibleObligations, 2, BigDecimal.ROUND_HALF_UP).subtract(BigDecimal.ONE).multiply(BigDecimal.TEN).intValue();
        }
        return 0;
    }

    @Override
    public Integer autosuggest(Application application, CarPrice price, double interestRate, double rate, double interestFrom, double interestTo, int yearFrom, int yearTo) {

        int pointsForLoan = loanAmountEvaluation(price, application.getLoanAmount());
        int pointsForPayment = paymentEvaluation(application, calculateInterestRate(application, interestFrom, interestTo, yearFrom, yearTo), rate);
        int evaluation = pointsForLoan + pointsForPayment;
        save(application, evaluation);
        return evaluation;
    }

    private void save(Application application, Integer evaluation) {
        Autosuggestor autosuggestion = new Autosuggestor();
        autosuggestion.setApplication(application);
        autosuggestion.setEvaluation(evaluation);

        autosuggestorRepository.save(autosuggestion);
    }
}