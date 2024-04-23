package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "autosuggestors")
public class Autosuggestor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "application_id")
    private Application application;

    private int evaluation;

    private int currentYear = LocalDate.now().getYear();

    private double rate;

}