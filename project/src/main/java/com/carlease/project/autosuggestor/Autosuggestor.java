package com.carlease.project.autosuggestor;

import com.carlease.project.application.Application;
import com.carlease.project.enums.AutosuggestionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "evaluation_status")
    @NotNull
    @Enumerated(EnumType.STRING)
    private AutosuggestionStatus evalStatus;

    private int currentYear;

}