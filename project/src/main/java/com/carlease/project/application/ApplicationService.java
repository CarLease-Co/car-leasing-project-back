package com.carlease.project.application;

import com.carlease.project.user.User;

import java.util.List;

public interface ApplicationService {
    List<Application> findAll();
    Application findById(long id);
    Application create(Application application);

    List<Application> findAllByUserId(long id);
}
