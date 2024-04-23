package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class AutosuggestorDto {

    private Long id;
    private Application application;
    private int evaluation;
    private double interestRate;
    private int currentYear = LocalDate.now().getYear();
    private double rate;
    private CarPrice price;
}
