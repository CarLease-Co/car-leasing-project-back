package com.carlease.project.interestrate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/interest-rates")
public class InterestRateController {
    private final InterestRateService interestRateService;

    @Autowired
    public InterestRateController(InterestRateService interestRateService) {
        this.interestRateService = interestRateService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<InterestRate>> getAllInterestRates() {
        List<InterestRate> interestRates = interestRateService.findAll();
        return new ResponseEntity<>(interestRates, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<InterestRate> updateOrCreateInterestRate(@RequestBody InterestRate interestRate) {
        InterestRate updatedInterestRate = interestRateService.findAndUpdate(interestRate);
        return new ResponseEntity<>(updatedInterestRate, HttpStatus.OK);
    }
}
