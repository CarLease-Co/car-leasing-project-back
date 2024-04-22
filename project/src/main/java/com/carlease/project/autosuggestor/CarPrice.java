package com.carlease.project.autosuggestor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CarPrice {

    private BigDecimal avgPrice;
    private BigDecimal avgMin;
    private BigDecimal avgMax;
}
