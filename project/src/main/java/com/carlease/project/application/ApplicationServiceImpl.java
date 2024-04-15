package com.carlease.project.application;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public List<Application> findAllApplicationByApplicantId(int applicantId) {
        return applicationRepository.findAllByApplicantId(applicantId);
    }
}
