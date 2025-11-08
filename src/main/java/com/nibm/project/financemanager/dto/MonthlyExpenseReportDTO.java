package com.nibm.project.financemanager.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Adds getters, setters, toString, etc.
@NoArgsConstructor // Adds the default constructor Spring needs
public class MonthlyExpenseReportDTO {
    private String month;
    private String category;
    private BigDecimal totalSpent;
}