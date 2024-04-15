package com.carlease.project.application;

import java.util.List;

public interface ApplicationService {
    List<Application> findAllApplicationByApplicantId(int applicantId);
}
