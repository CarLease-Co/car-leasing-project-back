package com.carlease.project.interestrate;

import java.util.List;

public interface InterestRateService {
    List<InterestRateDTO> findAll();
    InterestRateDTO findAndUpdate(InterestRateDTO interestRateDTO);
}
