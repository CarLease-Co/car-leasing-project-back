package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;
import com.carlease.project.application.ApplicationFormDto;
import com.carlease.project.application.ApplicationServiceImpl;
import com.carlease.project.car.CarDto;
import com.carlease.project.interestrate.InterestRate;
import com.carlease.project.user.exceptions.AutosuggestorNotFoundException;
import com.carlease.project.user.exceptions.CarNotFoundException;
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

    @GetMapping(produces = "application/json")
    ResponseEntity<List<AutosuggestorDto>> getAutosuggestions() {
        List<AutosuggestorDto> list = autosuggestorService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}

