package com.carlease.project.interestrate;

import com.carlease.project.enums.UserRole;
import com.carlease.project.exceptions.UserException;
import com.carlease.project.exceptions.UserNotFoundException;

import java.util.List;

public interface InterestRateService {
    List<InterestRateDTO> findAll();
    InterestRateDTO findAndUpdate(InterestRateDTO interestRateDTO, long userId, UserRole role) throws UserNotFoundException, UserException;
}
