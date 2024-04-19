package com.carlease.project.application;

import com.carlease.project.car.Car;
import com.carlease.project.user.User;
import com.carlease.project.enums.ApplicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "applications")
@Data
public class Application {


    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "monthly_income")
    @NotNull
    private BigDecimal monthlyIncome;

    @Column(name = "financial_obligations")
    @NotNull
    private BigDecimal financialObligations;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "manufacture_date")
    @NotNull
    private int manufactureDate;

    @Column(name = "free_text_explanation")
    private String textExplanation;

    @Column(name="loan_duration")
    @NotNull
    private int loanDuration;

    @Column(name = "loan_amount")
    @NotNull
    private BigDecimal loanAmount;

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private LocalDate endDate;
}
