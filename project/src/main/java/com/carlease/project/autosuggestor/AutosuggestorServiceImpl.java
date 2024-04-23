package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AutosuggestorServiceImpl implements IAutosuggestorService {

    private final AutosuggestorRepository autosuggestorRepository;

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

    @Override
    public Integer autosuggest(Application application, double interestRate, int currentYear, double rate, double interestFrom, double interestTo, int yearFrom, int yearTo) {
        // kiek lieka pinigu naudoti per menesi
        BigDecimal amountLeftToUse = application.getMonthlyIncome().subtract(application.getFinancialObligations());

        // kokia menesine imoka norimai paskolai
        BigDecimal monthlyPayment = calculateTotalLoanPrice(application, calculateInterestRate(application, interestFrom, interestTo, yearFrom, yearTo)).divide(BigDecimal.valueOf(application.getLoanDuration()));

        //Kiek gali isvis tureti menesiniu isipareigojimu
        BigDecimal maxPossibleObligations = application.getMonthlyIncome().multiply(BigDecimal.valueOf(rate));

        int evaluation = 0;

        if (application.getMonthlyIncome().subtract(application.getFinancialObligations()).compareTo(maxPossibleObligations) == 0) {
            evaluation = 0;
        } else if (application.getMonthlyIncome().compareTo(maxPossibleObligations) == 1) {

        }

        return null;
    }


}