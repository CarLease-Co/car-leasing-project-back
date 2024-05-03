package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;
import com.carlease.project.enums.AutosuggestionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AutosuggestorDto {

    private Long id;
    private Application application;
    private int evaluation;
    private int currentYear;
    private CarPrice price;
    private AutosuggestionStatus evalStatus;
}
