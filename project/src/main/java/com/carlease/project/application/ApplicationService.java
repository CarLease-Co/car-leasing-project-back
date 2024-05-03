package com.carlease.project.application;

import com.carlease.project.autosuggestor.AutosuggestorDto;
import com.carlease.project.enums.ApplicationStatus;
import com.carlease.project.enums.UserRole;
import com.carlease.project.exceptions.*;

import java.util.List;

public interface ApplicationService {
    List<ApplicationFormDto> findAll();

    ApplicationFormDto findById(long id) throws ApplicationNotFoundException;

    List<ApplicationFormDto> findAllByStatus(ApplicationStatus status);

    List<ApplicationFormDto> findAllByStatuses(List<ApplicationStatus> statuses);

    List<ApplicationFormDto> getApplicationsByUser(long id, UserRole role) throws UserException, UserNotFoundException;

    void evaluation(ApplicationFormDto applicationDto);

    ApplicationFormDto create(ApplicationFormDto applicationFormDto, long userId, UserRole role) throws UserNotFoundException, UserException;

    ApplicationFormDto updateStatus(long applicationId, long userId, ApplicationStatus status, UserRole role) throws ApplicationNotFoundException, UserException;

    List<ApplicationFormDto> findAllByUserId(long id);

    AutosuggestorDto findAutosuggestorByApplicationId(long id) throws AutosuggestorNotFoundException, ApplicationNotFoundException;

    boolean deleteById(long applicationId, long userId, UserRole role) throws ApplicationNotFoundException, UserException, UserNotFoundException;

    ApplicationFormDto update(long id, ApplicationFormDto applicationFormDto,long userId, UserRole role) throws ApplicationNotFoundException, ApplicationNotDraftException, UserException, UserNotFoundException;
}
