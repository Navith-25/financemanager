package com.nibm.project.financemanager.dto;
import java.time.LocalDate;


public record TransactionDTO(
        Long id,
        String description,
        Double amount,
        String category,
        LocalDate date,
        boolean isSynced
) {}