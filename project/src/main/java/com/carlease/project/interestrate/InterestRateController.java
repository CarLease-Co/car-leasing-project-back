package com.carlease.project.interestrate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/interest-rates")
public class InterestRateController {

    private final InterestRateService interestRateService;
    private final InterestRateMapper interestRateMapper;

    @Autowired
    public InterestRateController(InterestRateService interestRateService, InterestRateMapper interestRateMapper) {
        this.interestRateService = interestRateService;
        this.interestRateMapper = interestRateMapper;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<InterestRateDTO>> getAllInterestRates() {
        List<InterestRate> interestRates = interestRateService.findAll();
        List<InterestRateDTO> interestRateDTOs = interestRates.stream()
                .map(interestRateMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(interestRateDTOs, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<InterestRateDTO> updateOrCreateInterestRate(@RequestBody InterestRateDTO interestRateDTO) {
        InterestRate updatedInterestRate = interestRateService.findAndUpdate(interestRateMapper.toEntity(interestRateDTO));
        return new ResponseEntity<>(interestRateMapper.toDto(updatedInterestRate), HttpStatus.OK);
    }
}
