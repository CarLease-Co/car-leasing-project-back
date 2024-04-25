package com.carlease.project.application;

import com.carlease.project.user.exceptions.ApplicationNotFoundException;

import java.util.List;

public interface ApplicationService {
    List<ApplicationFormDto> findAll();
    ApplicationFormDto findById(long id) throws ApplicationNotFoundException;
    ApplicationFormDto create(ApplicationFormDto applicationFormDto);
    void evaluation(ApplicationFormDto applicationDto);
    List<ApplicationFormDto> findAllByUserId(long id);
}
