package com.carlease.project.car;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarDto {
    private long id;
    private String make;
    private String model;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
}
