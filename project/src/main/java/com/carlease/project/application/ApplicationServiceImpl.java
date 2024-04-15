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
    public List<Application> findAll() {
        return applicationRepository.findAll();
    }

    @Override
    public Application findById(long id) {
        return applicationRepository.findById(id).orElseThrow();
    }

    @Override
    public Application create(Application application) {
        return applicationRepository.save(application);
    }


}
