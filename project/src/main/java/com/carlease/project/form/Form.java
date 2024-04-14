package com.carlease.project.form;

import java.math.BigDecimal;

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
