package com.nibm.project.financemanager.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryExpenseReportDTO {
    private String category;
    private BigDecimal totalSpent;
    private BigDecimal percentageOfTotal;
}