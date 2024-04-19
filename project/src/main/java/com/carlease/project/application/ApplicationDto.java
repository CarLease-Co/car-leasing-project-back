package com.carlease.project.application;

import com.carlease.project.car.Car;
import com.carlease.project.car.CarDto;
import com.carlease.project.enums.ApplicationStatus;
import com.carlease.project.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ApplicationDto {
    private long userId;
    private BigDecimal monthlyIncome;
    private BigDecimal financialObligations;
    private CarDto carDto;
    private int manufactureDate;
    private String textExplanation;
    private int loanDuration;
    private BigDecimal loanAmount;
    private LocalDate startDate;
}
