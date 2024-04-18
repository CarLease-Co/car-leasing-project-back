package com.carlease.project.car;

import com.carlease.project.application.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cars")
public class Car {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String make;
    @NotBlank
    private String model;

    private BigDecimal priceFrom;
    private BigDecimal priceTo;

    @OneToMany(mappedBy = "car")
    @JsonIgnore
    private List<Application> applications;
}
