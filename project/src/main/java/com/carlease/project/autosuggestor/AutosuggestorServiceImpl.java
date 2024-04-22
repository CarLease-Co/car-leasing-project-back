package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AutosuggestorServiceImpl implements IAutosuggestorService {
    private static final float PERCENT = 0.1f;
    private final AutosuggestorRepository autosuggestorRepository;
    public AutosuggestorServiceImpl(AutosuggestorRepository autosuggestorRepository) {
        this.autosuggestorRepository = autosuggestorRepository;
    }

    @Override
    public Autosuggestor findById(long id) {
        return autosuggestorRepository.findById(id).orElseThrow();
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

        BigDecimal difference = maxPrice.divide(carPriceUpToTenYears, 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal carPriceUpToTwentyYears = carPriceUpToTenYears.divide(difference, 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal carPriceUpToThirtyYears = carPriceUpToTwentyYears.divide(difference, 2, BigDecimal.ROUND_HALF_UP);

        BigDecimal priceChangePerYearUpToTwentyYears = carPriceUpToTenYears.subtract(carPriceUpToTwentyYears).divide(BigDecimal.TEN);

        BigDecimal priceChangePerYearUpToThirtyYears = carPriceUpToTwentyYears.subtract(carPriceUpToThirtyYears).divide(BigDecimal.TEN);

        if (carYear > currentYear - 10) {
            int carOld = currentYear - carYear;
            carPrice = maxPrice.subtract(BigDecimal.valueOf(carOld).multiply(priceChangePerYearUpToTenYears));

        } else if (carYear > currentYear - 20) {
            int carOld = currentYear - 10 - carYear;
            carPrice = carPriceUpToTenYears.subtract(BigDecimal.valueOf(carOld).multiply(priceChangePerYearUpToTwentyYears));

        } else {
            int carOld = currentYear - 20 - carYear;
            carPrice = carPriceUpToTwentyYears.subtract(BigDecimal.valueOf(carOld).multiply(priceChangePerYearUpToThirtyYears));
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
            return -loanEvaluationRange.divide(evaluationRange).subtract(BigDecimal.ONE).intValue();
        } else if (price.getAvgMin().compareTo(loanAmount) == 1) {
            BigDecimal evaluationRange = price.getAvgPrice().subtract(price.getAvgMin());
            BigDecimal loanEvaluationRange = price.getAvgPrice().subtract(loanAmount);
            return loanEvaluationRange.divide(evaluationRange).subtract(BigDecimal.ONE).intValue();
        }
        return 0;
    }

    public Integer paymentEvaluation(Application application, double interestRate, double rate) {
        BigDecimal monthlyPayment = calculateTotalLoanPrice(application, interestRate).divide(BigDecimal.valueOf(application.getLoanDuration()));

        BigDecimal maxPossibleObligations = application.getMonthlyIncome().subtract(application.getFinancialObligations().multiply(BigDecimal.valueOf(rate)));

        if (maxPossibleObligations.compareTo(monthlyPayment) == 1) {
            return BigDecimal.ONE.subtract(monthlyPayment.divide(maxPossibleObligations)).multiply(BigDecimal.TEN).intValue();
        } else if (monthlyPayment.compareTo(maxPossibleObligations) == 1) {
            return -monthlyPayment.divide(maxPossibleObligations).subtract(BigDecimal.ONE).multiply(BigDecimal.TEN).intValue();
        }
        return 0;
    }

    @Override
    public Integer autosuggest(Application application, CarPrice price, double interestRate, double rate) {

        int pointsForLoan = loanAmountEvaluation(price, application.getLoanAmount());
        int pointsForPayment = paymentEvaluation(application, interestRate, rate);
        return pointsForLoan + pointsForPayment;
    }
}