package com.carlease.project.autosuggestor;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InterestRate {
    private double interestFrom;
    private double interestTo;
    private int yearFrom;
    private int yearTo;
}
