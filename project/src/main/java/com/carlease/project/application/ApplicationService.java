package com.carlease.project.application;

import com.carlease.project.autosuggestor.AutosuggestorDto;
import com.carlease.project.enums.ApplicationStatus;
import com.carlease.project.enums.UserRole;
import com.carlease.project.user.exceptions.ApplicationNotFoundException;
import com.carlease.project.user.exceptions.AutosuggestorNotFoundException;
import com.carlease.project.user.exceptions.UserNotFoundException;

import java.util.List;

public interface ApplicationService {
    List<ApplicationFormDto> findAll();
    ApplicationFormDto findById(long id) throws ApplicationNotFoundException;
    ApplicationFormDto create(ApplicationFormDto applicationFormDto) throws UserNotFoundException;

    List<ApplicationFormDto> findAllByStatus(ApplicationStatus status);

    List<ApplicationFormDto> findAllByStatusIn(List<ApplicationStatus> statuses);

    List<ApplicationFormDto> getApplicationsByUser(long id, UserRole role);

    void evaluation(ApplicationFormDto applicationDto);
    List<ApplicationFormDto> findAllByUserId(long id);
    AutosuggestorDto findAutosuggestorByApplicationId(long id) throws AutosuggestorNotFoundException;

}
