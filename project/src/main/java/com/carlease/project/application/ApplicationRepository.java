package com.carlease.project.application;

import com.carlease.project.enums.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    List<Application> findApplicationsByUserUserId(long id);

    List<Application> findByStatus(ApplicationStatus status);

    List<Application> findByStatuses(List<ApplicationStatus> statuses);
}
