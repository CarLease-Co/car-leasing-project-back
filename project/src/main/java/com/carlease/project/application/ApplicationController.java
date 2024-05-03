package com.carlease.project.application;

import com.carlease.project.autosuggestor.AutosuggestorDto;
import com.carlease.project.enums.ApplicationStatus;
import com.carlease.project.enums.UserRole;
import com.carlease.project.exceptions.*;
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

    @GetMapping("/applications")
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

    @GetMapping
    public ResponseEntity<?> getApplicationsByUser(@RequestHeader("userId") long userId, @RequestHeader("role") UserRole role) {
        try {
            List<ApplicationFormDto> applicationDTOs = applicationService.getApplicationsByUser(userId, role);
            return new ResponseEntity<>(applicationDTOs, HttpStatus.OK);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    ResponseEntity<?> createApplication(@RequestBody ApplicationFormDto applicationFormDto, @RequestHeader("userId") long userId, @RequestHeader("role") UserRole role) throws UserNotFoundException {
        try {
            ApplicationFormDto newApplication = applicationService.create(applicationFormDto, userId, role);
            applicationService.evaluation(newApplication);
            return new ResponseEntity<>(newApplication, HttpStatus.CREATED);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateStatus(@PathVariable("id") long applicationId, @RequestBody ApplicationStatus status, @RequestHeader("userId") long userId, @RequestHeader("role") UserRole role) {
        try {
            ApplicationFormDto updatedApplication = applicationService.updateStatus(applicationId, userId, status, role);
            if (ApplicationStatus.PENDING.equals(updatedApplication.getStatus())) {
                applicationService.evaluation(updatedApplication);
            }
            return ResponseEntity.ok(updatedApplication);
        } catch (ApplicationNotFoundException | UserException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}/autosuggestion")
    ResponseEntity<AutosuggestorDto> getAutosuggestionByApplicationId(@PathVariable("id") long id) throws AutosuggestorNotFoundException, ApplicationNotFoundException {
        AutosuggestorDto autosuggestion = applicationService.findAutosuggestorByApplicationId(id);
        return new ResponseEntity<>(autosuggestion, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteApplication(@PathVariable("id") long applicationId, @RequestHeader("userId") long userId, @RequestHeader("role") UserRole role) throws ApplicationNotFoundException, UserException, UserNotFoundException {
        boolean applicationDeleted = applicationService.deleteById(applicationId, userId, role);
        if (applicationDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<ApplicationFormDto> update(@PathVariable("id") long applicationId, @RequestBody ApplicationFormDto applicationDto, @RequestHeader("userId") long userId, @RequestHeader("role") UserRole role) throws ApplicationNotFoundException, ApplicationNotDraftException, UserException, UserNotFoundException {
        ApplicationFormDto updatedApplication = applicationService.update(applicationId, applicationDto, userId, role);
        if (ApplicationStatus.PENDING.equals(updatedApplication.getStatus())) {
            applicationService.evaluation(updatedApplication);
        }
        return ResponseEntity.ok(updatedApplication);
    }
}
