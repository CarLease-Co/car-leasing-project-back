package com.carlease.project.application;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ApplicationFormDto {
    private long userId;
    private BigDecimal monthlyIncome;
    private BigDecimal financialObligations;
    private String carMake;
    private String carModel;
    private int manufactureDate;
    private String textExplanation;
    private int loanDuration;
    private BigDecimal loanAmount;
    private LocalDate startDate;
}
