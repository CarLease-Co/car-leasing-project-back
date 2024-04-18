package com.carlease.project.application;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/applications")
public class ApplicationController {

    public ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping(produces = "application/json")
    ResponseEntity<List<Application>> getApplications() {
        List<Application> list = applicationService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Application> getApplication(@PathVariable("id") long id) {

        Application application = applicationService.findById(id);
        return new ResponseEntity<>(application, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Application> createApplication(@RequestBody Application application) {
        Application newApplication = applicationService.create(application);
        return new ResponseEntity<>(newApplication, HttpStatus.CREATED);
    }
}