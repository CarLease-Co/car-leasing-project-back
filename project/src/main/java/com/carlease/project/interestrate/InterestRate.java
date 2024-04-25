package com.carlease.project.interestrate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "interest_rates")
public class InterestRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double rate;
    private double interestFrom;
    private double interestTo;
    private int yearFrom;
    private int yearTo;

}
