package com.carlease.project.interestrate;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterestRateServiceImpl implements InterestRateService{

    private final InterestRateRepository interestRateRepository;

    public InterestRateServiceImpl(InterestRateRepository interestRateRepository) {
        this.interestRateRepository = interestRateRepository;
    }

    @Override
    public List<InterestRate> findAll() {
        return interestRateRepository.findAll();
    }

    @Override
    public InterestRate findAndUpdate(InterestRate interestRate) {
        List<InterestRate> existingInterestRates = interestRateRepository.findAll();
        if (!existingInterestRates.isEmpty()) {
            InterestRate existingInterestRate = existingInterestRates.get(0);
            existingInterestRate.setInterestFrom(interestRate.getInterestFrom());
            existingInterestRate.setInterestTo(interestRate.getInterestTo());
            existingInterestRate.setYearFrom(interestRate.getYearFrom());
            existingInterestRate.setYearTo(interestRate.getYearTo());
            return interestRateRepository.save(existingInterestRate);
        } else {
            return interestRateRepository.save(interestRate);
        }
    }
}
