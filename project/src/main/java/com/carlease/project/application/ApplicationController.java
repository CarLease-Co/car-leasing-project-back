package com.carlease.project.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class ApplicationController {

    public ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping(name="/application/all/{applicant_id}")
    public ResponseEntity<List<Application>> getAllUserApplications(@PathVariable int applicantId) {
        List<Application> applications = applicationService.findAllApplicationByApplicantId(applicantId);
        if (applications == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        return ResponseEntity.ok(applications);
    }
}
