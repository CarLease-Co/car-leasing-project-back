package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;
import com.carlease.project.application.ApplicationServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/autosuggestors")
public class AutosuggestorController {
    @Autowired
    private final AutosuggestorServiceImpl autosuggestorService;
    @Autowired
    private final ApplicationServiceImpl applicationService;

    @PostMapping("/{applicationId}/{interestRate}/{rate}")
    public Integer evaluate(@PathVariable Long applicationId, @PathVariable InterestRate interestRate, @PathVariable double rate) {
        Application existingApplication = applicationService.findById(applicationId);
        return autosuggestorService.autosuggest(existingApplication, autosuggestorService.carPrice(BigDecimal.valueOf(10000)), rate, interestRate);
    }

    @GetMapping(produces = "application/json")
    ResponseEntity<List<Autosuggestor>> getAutosuggestions() {
        List<Autosuggestor> list = autosuggestorService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}

