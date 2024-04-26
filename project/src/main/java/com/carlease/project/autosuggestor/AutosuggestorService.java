package com.carlease.project.autosuggestor;

import com.carlease.project.application.ApplicationFormDto;
import com.carlease.project.interestrate.InterestRate;
import com.carlease.project.user.exceptions.AutosuggestorNotFoundException;

import java.math.BigDecimal;
import java.util.List;

public interface AutosuggestorService {
    AutosuggestorDto findById(long id) throws AutosuggestorNotFoundException;

    double calculateInterestRate(ApplicationFormDto applicationDto, InterestRate interestRate);

    List<AutosuggestorDto> findAll();

    BigDecimal calculateTotalLoanPrice(ApplicationFormDto applicationDto, double interestRate);

    BigDecimal calculateAverageCarPriceDependingOnYear(ApplicationFormDto applicationDto, int currentYear);

    CarPrice calculateAvgCarPriceRange(BigDecimal price);

    Integer autosuggest(ApplicationFormDto applicationDto, CarPrice price, InterestRate interestRate);
}