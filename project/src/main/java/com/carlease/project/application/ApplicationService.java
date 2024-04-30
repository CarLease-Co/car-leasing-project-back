package com.carlease.project.application;

import com.carlease.project.autosuggestor.AutosuggestorDto;
import com.carlease.project.enums.ApplicationStatus;
import com.carlease.project.enums.UserRole;
import com.carlease.project.exceptions.ApplicationNotFoundException;
import com.carlease.project.exceptions.AutosuggestorNotFoundException;
import com.carlease.project.exceptions.UserException;
import com.carlease.project.exceptions.UserNotFoundException;

import java.util.List;

public interface ApplicationService {
    List<ApplicationFormDto> findAll();

    ApplicationFormDto findById(long id) throws ApplicationNotFoundException;

    ApplicationFormDto create(ApplicationFormDto applicationFormDto) throws UserNotFoundException;

    List<ApplicationFormDto> findAllByStatus(ApplicationStatus status);

    List<ApplicationFormDto> findAllByStatuses(List<ApplicationStatus> statuses);

    List<ApplicationFormDto> getApplicationsByUser(long id, UserRole role) throws UserException;

    void evaluation(ApplicationFormDto applicationDto);

    ApplicationFormDto updateStatus(long id, ApplicationStatus status) throws ApplicationNotFoundException;

    List<ApplicationFormDto> findAllByUserId(long id);

    AutosuggestorDto findAutosuggestorByApplicationId(long id) throws AutosuggestorNotFoundException, ApplicationNotFoundException;

    boolean deleteById(long applicationId, long userId, UserRole role) throws ApplicationNotFoundException, UserException;

}
