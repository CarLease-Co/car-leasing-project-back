package com.carlease.project.application;

import com.carlease.project.autosuggestor.AutosuggestorDto;
import com.carlease.project.user.exceptions.ApplicationNotFoundException;
import com.carlease.project.user.exceptions.AutosuggestorNotFoundException;
import com.carlease.project.user.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/applications")
public class ApplicationController {

    public ApplicationService applicationService;

    @Autowired
    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping(produces = "application/json")
    ResponseEntity<List<ApplicationFormDto>> getApplications() {
        List<ApplicationFormDto> list = applicationService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<ApplicationFormDto> getApplication(@PathVariable("id") long id) throws ApplicationNotFoundException {

        ApplicationFormDto applicationDto = applicationService.findById(id);
        return new ResponseEntity<>(applicationDto, HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    ResponseEntity<List<ApplicationFormDto>> getApplicationsByUserId(@PathVariable("userId") long id) {
        List<ApplicationFormDto> applications = applicationService.findAllByUserId(id);
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<ApplicationFormDto> createApplication(@RequestBody ApplicationFormDto applicationFormDto) throws UserNotFoundException {
        ApplicationFormDto newApplication = applicationService.create(applicationFormDto);
        applicationService.evaluation(newApplication);
        return new ResponseEntity<>(newApplication, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/autosuggestion")
    ResponseEntity<AutosuggestorDto> getAutosuggestionByApplicationId(@PathVariable("id") long id) throws AutosuggestorNotFoundException {
        AutosuggestorDto autosuggestion = applicationService.findAutosuggestorByApplicationId(id);
        return new ResponseEntity<>(autosuggestion, HttpStatus.OK);
    }
}