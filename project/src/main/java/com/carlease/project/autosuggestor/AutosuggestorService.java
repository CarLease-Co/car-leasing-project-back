package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;
import com.carlease.project.application.ApplicationFormDto;

import java.math.BigDecimal;
import java.util.List;

public interface AutosuggestorService {
    Autosuggestor findById(long id);

    double calculateInterestRate(Application application, InterestRate interestRate);

    List<Autosuggestor> findAll();

    BigDecimal calculateTotalLoanPrice(Application application, double interestRate);

    BigDecimal calculateAverageCarPriceDependingOnYear(Application application, int currentYear);

    CarPrice carPrice (BigDecimal price);

    Integer autosuggest(Application application, CarPrice price, double rate, InterestRate interestRate);
}