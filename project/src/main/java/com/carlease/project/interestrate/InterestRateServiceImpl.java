package com.carlease.project.interestrate;

import com.carlease.project.enums.UserRole;
import com.carlease.project.exceptions.UserException;
import com.carlease.project.exceptions.UserNotFoundException;
import com.carlease.project.user.UserRepository;
import com.carlease.project.user.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterestRateServiceImpl implements InterestRateService {

    private final InterestRateRepository interestRateRepository;
    private final InterestRateMapper interestRateMapper;
    private final UserRepository userRepository;

    @Autowired
    public InterestRateServiceImpl(InterestRateRepository interestRateRepository, InterestRateMapper interestRateMapper, UserRepository userRepository) {
        this.interestRateRepository = interestRateRepository;
        this.interestRateMapper = interestRateMapper;
        this.userRepository = userRepository;
    }

    @Override
    public List<InterestRateDTO> findAll() {
        List<InterestRate> interestRates = interestRateRepository.findAll();
        return interestRates.stream()
                .map(interestRateMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public InterestRateDTO findAndUpdate(InterestRateDTO interestRateDTO, long userId, UserRole role) throws UserNotFoundException, UserException {
        UserServiceImpl.validateUserRole(userRepository, userId, role);

        if (!UserRole.BUSINESS_ADMIN.equals(role))
            throw new UserException("User role does not match the provided role");

        InterestRate interestRate = interestRateMapper.toEntity(interestRateDTO);

        List<InterestRate> existingInterestRates = interestRateRepository.findAll();
        if (!existingInterestRates.isEmpty()) {
            InterestRate existingInterestRate = existingInterestRates.getFirst();
            existingInterestRate.setRate(interestRate.getRate());
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
