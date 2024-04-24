package com.carlease.project.interestrate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "interest_rates")
public class InterestRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double interestFrom;
    private double interestTo;
    private int yearFrom;
    private int yearTo;

    public InterestRate(double interestFrom, double interestTo, int yearFrom, int yearTo) {
        this.interestFrom = interestFrom;
        this.interestTo = interestTo;
        this.yearFrom = yearFrom;
        this.yearTo = yearTo;
    }
}
