package com.nibm.project.financemanager.dto;

import java.math.BigDecimal;

public record SavingsProgressReportDTO(
        String name,
        BigDecimal targetAmount,
        BigDecimal currentAmount,
        BigDecimal progressPercentage
) {}