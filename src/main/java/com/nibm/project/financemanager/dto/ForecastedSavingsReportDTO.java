package com.nibm.project.financemanager.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForecastedSavingsReportDTO {
    private String forecastMonth;
    private BigDecimal forecastedSavings;
}