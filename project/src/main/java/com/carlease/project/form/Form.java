package com.carlease.project.form;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Form {

    private BigDecimal income;
    private BigDecimal financialObligation;
    private String carBrand;
    private String carModel;
    private BigDecimal leasingAmount;
    private Integer leasingPeriod;
    private String explanation;

    public boolean isValid() {
        return true;
    }
}
