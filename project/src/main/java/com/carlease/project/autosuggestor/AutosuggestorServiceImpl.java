package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    public BigDecimal calculateTotalLoanPrice(Application application, double interestRate) {

        int loanDuration = application.getLoanDuration();
        BigDecimal loanAmount = application.getLoanAmount();
        double interestRateValue = interestRate / 100 + 1;
        BigDecimal totalInterestValue = BigDecimal.valueOf(Math.pow(interestRateValue, loanDuration));
        BigDecimal totalInterest = loanAmount.multiply(totalInterestValue);
        return loanAmount.add(totalInterest);
    }

    public BigDecimal calculateAverageCarPriceDependingOnYear(Application application, int currentYear) {
        BigDecimal carPrice;

        BigDecimal maxPrice = application.getCar().getPriceTo();
        BigDecimal carPriceUpToTenYears = application.getCar().getPriceFrom();

        int carYear = application.getManufactureDate();

        // kiek kaina pasikeicia per vienus metus per pirmus 10 metu
        BigDecimal priceChangePerYear = (carPriceUpToTenYears.add(maxPrice)).divide(BigDecimal.TEN);

        //kiek KARTU pasikeicia kaina per pirmus 10 metu
        BigDecimal difference = maxPrice.divide(carPriceUpToTenYears);

        // kiek kainuos automobilis 20 metu senumo
        BigDecimal carPriceUpToTwentyYears = carPriceUpToTenYears.divide(difference);

        // kiek kainuos automobilis 30 metu senumo
        BigDecimal carPriceUpToThirtyYears = carPriceUpToTwentyYears.divide(difference);

        //Kiek kaina pasikeicia jei auto tarp 10 ir 20 metu senumo
        BigDecimal priceChangePerYearTwentyYearsBefore = (carPriceUpToTenYears.add(carPriceUpToTwentyYears)).divide(BigDecimal.TEN);

        //Kiek kaina pasikeicia jei auto tarp 20 ir 30 metu senumo
        BigDecimal priceChangePerYearThirtyYearsBefore = (carPriceUpToTwentyYears.add(carPriceUpToThirtyYears)).divide(BigDecimal.TEN);

        if (carYear > currentYear - 10) {
            int carOld = currentYear - carYear;
            carPrice = maxPrice.subtract(BigDecimal.valueOf(carOld).multiply(priceChangePerYear));

        } else if (carYear > currentYear - 20) {
            int carOld = currentYear - 10 - carYear;
            carPrice = maxPrice.subtract(BigDecimal.valueOf(carOld).multiply(priceChangePerYearTwentyYearsBefore));

        } else {
            int carOld = currentYear - 20 - carYear;
            carPrice = maxPrice.subtract(BigDecimal.valueOf(carOld).multiply(priceChangePerYearThirtyYearsBefore));
        }
        return carPrice;
    }

//    Given,   (x1,y1) = (1,4)
//             (x2,y2) = (6,9)
//    x = 5

//    From Linear Interpolation formula,
//    y = y1+ (((x-x1) x (y2-y1))/ (x2-x1))
//
//     x = carYear -> application.getManufactureDate();
//    x1  -> currentYear
//    x2  -> currentYear-10
//    y1 -> application.getCar().getPriceTo()
//    y2 -> application.getCar().getPriceFrom()
//    y??

    @Override
    public Integer autosuggest(Application application, double interestRate, int currentYear, double rate) {
        // kiek lieka pinigu naudoti per menesi
        BigDecimal amountLeftToUse = application.getMonthlyIncome().subtract(application.getFinancialObligations());

        // kokia menesine imoka norimai paskolai
        BigDecimal monthlyPayment = calculateTotalLoanPrice(application, interestRate).divide(BigDecimal.valueOf(application.getLoanDuration()));

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