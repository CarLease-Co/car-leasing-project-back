package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;
import com.carlease.project.application.ApplicationFormDto;

import java.math.BigDecimal;
import java.util.List;

public interface AutosuggestorService {
    Autosuggestor findById(long id);
    List<Autosuggestor> findAll();

    double calculateInterestRate(Application application, double interestFrom, double interestTo, int yearFrom, int yearTo);

    BigDecimal calculateTotalLoanPrice(Application application, double interestRate);

    BigDecimal calculateAverageCarPriceDependingOnYear(Application application, int currentYear);

    CarPrice carPrice (BigDecimal price);

    Integer autosuggest(Application application, CarPrice price, double interestRate, double rate, double interestFrom, double interestTo, int yearFrom, int yearTo);
}