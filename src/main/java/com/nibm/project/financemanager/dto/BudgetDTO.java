package com.nibm.project.financemanager.dto;
public record BudgetDTO(
        Long id,
        String category,
        Double amount,
        boolean isSynced
) {}
