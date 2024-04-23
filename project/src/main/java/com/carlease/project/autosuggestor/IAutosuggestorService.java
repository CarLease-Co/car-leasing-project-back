package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;

import java.math.BigDecimal;

public interface IAutosuggestorService {
    Autosuggestor findById(long id);

    double calculateInterestRate(Application application, double interestFrom, double interestTo, int yearFrom, int yearTo);

    BigDecimal calculateTotalLoanPrice(Application application, double interestRate);

    BigDecimal calculateAverageCarPriceDependingOnYear(Application application, int currentYear);

    Integer autosuggest(Application application, double interestRate, int currentYear, double rate, double interestFrom, double interestTo, int yearFrom, int yearTo);
}