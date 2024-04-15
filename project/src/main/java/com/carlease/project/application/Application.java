package com.carlease.project.application;
import com.carlease.project.user.User;
import com.carlease.project.enums.ApplicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="applications")
@Data
public class Application {


    @Column(name = "application_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long applicationId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "monthly_income")
    @NotNull
    private BigDecimal monthlyIncome;

    @Column(name = "financial_obligations")
    @NotNull
    private BigDecimal financialObligations;

    @Column(name = "car_id")
    @NotNull
    private int carId;

    @Column(name = "loan_amount")
    @NotNull
    private BigDecimal loanAmount;

    @Column(name = "loan_duration")
    @NotNull
    @Min(1)
    @Max(120)
    private int loanDurationInMonths;

    @Column(name = "free_text_explanation")
    private String textExplanation;

    @Column(name = "is_submitted")
    @NotNull
    private boolean isSubmitted;

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;


}
