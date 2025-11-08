package com.nibm.project.financemanager.dto;

import java.math.BigDecimal;

public record BudgetAdherenceReportDTO(
        String category,
        String month,
        BigDecimal budgetedAmount,
        BigDecimal actualSpent,
        BigDecimal variance
) {}