package com.carlease.project.interestrate;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterestRateServiceImpl implements InterestRateService{

    private final InterestRateRepository interestRateRepository;
    private final InterestRateMapper interestRateMapper;

    public InterestRateServiceImpl(InterestRateRepository interestRateRepository, InterestRateMapper interestRateMapper) {
        this.interestRateRepository = interestRateRepository;
        this.interestRateMapper = interestRateMapper;
    }

    @Override
    public List<InterestRateDTO> findAll() {
        List<InterestRate> interestRates = interestRateRepository.findAll();
        return interestRates.stream()
                .map(interestRateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public InterestRateDTO findAndUpdate(InterestRateDTO interestRateDTO) {
        InterestRate interestRate = interestRateMapper.toEntity(interestRateDTO);

        List<InterestRate> existingInterestRates = interestRateRepository.findAll();
        if (!existingInterestRates.isEmpty()) {
            InterestRate existingInterestRate = existingInterestRates.getFirst();
            existingInterestRate.setInterestFrom(interestRate.getInterestFrom());
            existingInterestRate.setInterestTo(interestRate.getInterestTo());
            existingInterestRate.setYearFrom(interestRate.getYearFrom());
            existingInterestRate.setYearTo(interestRate.getYearTo());
            InterestRate updatedInterestRate = interestRateRepository.save(existingInterestRate);
            return interestRateMapper.toDto(updatedInterestRate);
        } else {
            InterestRate savedInterestRate = interestRateRepository.save(interestRate);
            return interestRateMapper.toDto(savedInterestRate);
        }
    }
}
