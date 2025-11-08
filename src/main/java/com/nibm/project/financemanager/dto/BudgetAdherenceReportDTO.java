package com.nibm.project.financemanager.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BudgetAdherenceReportDTO {
    private String category;
    private String month;
    private BigDecimal budgetedAmount;
    private BigDecimal actualSpent;
    private BigDecimal variance;
}