package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;

import java.math.BigDecimal;

public interface AutosuggestorService {
    Autosuggestor findById(long id);

    BigDecimal calculateTotalLoanPrice(Application application, double interestRate);

    BigDecimal calculateAverageCarPriceDependingOnYear(Application application, int currentYear);

    Integer autosuggest(Application application, double interestRate, int currentYear, double rate);
}