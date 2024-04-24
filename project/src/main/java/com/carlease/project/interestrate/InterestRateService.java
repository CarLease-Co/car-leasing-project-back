package com.carlease.project.interestrate;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InterestRateService {
    List<InterestRate> findAll();
    InterestRate findAndUpdate(InterestRate interestRate);
}
