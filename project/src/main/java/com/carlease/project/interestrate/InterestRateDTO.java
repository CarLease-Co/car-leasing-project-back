package com.carlease.project.interestrate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InterestRateDTO {
    private double interestFrom;
    private double interestTo;
    private int yearFrom;
    private int yearTo;
}
