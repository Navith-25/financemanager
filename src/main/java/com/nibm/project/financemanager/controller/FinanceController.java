package com.nibm.project.financemanager.controller;

import com.nibm.project.financemanager.dto.BudgetDTO;
import com.nibm.project.financemanager.dto.SavingsGoalDTO;
import com.nibm.project.financemanager.dto.TransactionDTO;
// Import the new DTOs and Service
import com.nibm.project.financemanager.dto.BudgetAdherenceReportDTO;
import com.nibm.project.financemanager.dto.MonthlyExpenseReportDTO;
import com.nibm.project.financemanager.dto.SavingsProgressReportDTO;
import com.nibm.project.financemanager.service.ReportService; // <-- Add
import com.nibm.project.financemanager.service.LocalFinanceService;
import com.nibm.project.financemanager.service.SyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FinanceController {

    @Autowired
    private LocalFinanceService localService;
    @Autowired
    private SyncService syncService;
    @Autowired
    private ReportService reportService; // <-- Add ReportService

    // --- Transaction Endpoints  ---
    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> addTransaction(@RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(localService.addTransaction(dto));
    }
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        return ResponseEntity.ok(localService.getAllTransactions());
    }

    // --- Budget Endpoints  ---
    @PostMapping("/budgets")
    public ResponseEntity<BudgetDTO> setBudget(@RequestBody BudgetDTO dto) {
        return ResponseEntity.ok(localService.setBudget(dto));
    }
    @GetMapping("/budgets")
    public ResponseEntity<List<BudgetDTO>> getBudgets() {
        return ResponseEntity.ok(localService.getAllBudgets());
    }

    // --- Savings Goal Endpoints  ---
    @PostMapping("/savings-goals")
    public ResponseEntity<SavingsGoalDTO> addSavingsGoal(@RequestBody SavingsGoalDTO dto) {
        return ResponseEntity.ok(localService.addSavingsGoal(dto));
    }
    @GetMapping("/savings-goals")
    public ResponseEntity<List<SavingsGoalDTO>> getSavingsGoals() {
        return ResponseEntity.ok(localService.getAllSavingsGoals());
    }

    // --- Sync Endpoint  ---
    @PostMapping("/sync")
    public ResponseEntity<Map<String, Integer>> synchronize() {
        return ResponseEntity.ok(syncService.synchronizeData());
    }

    // --- NEW REPORTING ENDPOINTS ---

    @GetMapping("/reports/monthly-expenses")
    public ResponseEntity<List<MonthlyExpenseReportDTO>> getMonthlyExpenseReport() {
        return ResponseEntity.ok(reportService.getMonthlyExpenseReport());
    }

    @GetMapping("/reports/budget-adherence")
    public ResponseEntity<List<BudgetAdherenceReportDTO>> getBudgetAdherenceReport() {
        return ResponseEntity.ok(reportService.getBudgetAdherenceReport());
    }

    @GetMapping("/reports/savings-progress")
    public ResponseEntity<List<SavingsProgressReportDTO>> getSavingsProgressReport() {
        return ResponseEntity.ok(reportService.getSavingsProgressReport());
    }
}