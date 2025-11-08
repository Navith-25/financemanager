package com.nibm.project.financemanager.dto;
public record SavingsGoalDTO(
        Long id,
        String name,
        Double targetAmount,
        Double currentAmount,
        boolean isSynced
) {}