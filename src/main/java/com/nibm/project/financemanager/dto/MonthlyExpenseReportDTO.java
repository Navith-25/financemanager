package com.nibm.project.financemanager.dto;

import java.math.BigDecimal;

// This record will hold the results of our SQL query
public record MonthlyExpenseReportDTO(
        String month,
        String category,
        BigDecimal totalSpent
) {}