package com.carlease.project.application;

import com.carlease.project.enums.ApplicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="applications")
@Data
public class ApplicationEntity {


    @Column(name = "application_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int applicationId;

    @Column(name = "applicant_id")
    @NotNull
    private int applicantId;

    @Column(name = "user_name")
    @NotBlank
    private String userName;

    @Column(name = "user_surname")
    private String userSurname;

    @Column(name = "monthly_income")
    private BigDecimal monthlyIncome;

    @Column(name = "financial_obligations")
    private BigDecimal financialObligations;

    @Column(name = "car_id")
    private int carId;

    @Column(name = "loan_amount")
    private BigDecimal loanAmount;

    @Column(name = "loan_duration")
    private int loanDurationInMonths;

    @Column(name = "free_text_explanation")
    private String textExplanation;

    @Column(name = "is_submitted")
    private boolean isSubmitted;

    @Column(name = "status")
    private ApplicationStatus status;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;


}
