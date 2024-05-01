package com.carlease.project.interestrate;

import com.carlease.project.enums.UserRole;
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
    public ResponseEntity<List<InterestRateDTO>> getAllInterestRates() {
        List<InterestRateDTO> interestRateDTOs = interestRateService.findAll();
        return new ResponseEntity<>(interestRateDTOs, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<InterestRateDTO> updateOrCreateInterestRate(@RequestBody InterestRateDTO interestRateDTO, @RequestHeader("userId") long userId, @RequestHeader("role") UserRole role) {
        InterestRateDTO updatedInterestRateDTO = interestRateService.findAndUpdate(interestRateDTO, userId, role);
        return new ResponseEntity<>(updatedInterestRateDTO, HttpStatus.OK);
    }
}
