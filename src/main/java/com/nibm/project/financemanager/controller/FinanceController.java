package com.nibm.project.financemanager.controller;

import com.nibm.project.financemanager.dto.BudgetDTO;
import com.nibm.project.financemanager.dto.SavingsGoalDTO;
import com.nibm.project.financemanager.dto.TransactionDTO;
import com.nibm.project.financemanager.dto.BudgetAdherenceReportDTO;
import com.nibm.project.financemanager.dto.MonthlyExpenseReportDTO;
import com.nibm.project.financemanager.dto.SavingsProgressReportDTO;
import com.nibm.project.financemanager.service.ReportService;
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
    private ReportService reportService;

    @PostMapping("/transactions")
    public ResponseEntity<TransactionDTO> addTransaction(@RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(localService.addTransaction(dto));
    }
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        return ResponseEntity.ok(localService.getAllTransactions());
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        syncService.deleteTransactionFromOracle(id);
        localService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/budgets")
    public ResponseEntity<BudgetDTO> setBudget(@RequestBody BudgetDTO dto) {
        return ResponseEntity.ok(localService.setBudget(dto));
    }
    @GetMapping("/budgets")
    public ResponseEntity<List<BudgetDTO>> getBudgets() {
        return ResponseEntity.ok(localService.getAllBudgets());
    }

    @DeleteMapping("/budgets/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        syncService.deleteBudgetFromOracle(id);
        localService.deleteBudget(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/savings-goals")
    public ResponseEntity<SavingsGoalDTO> addSavingsGoal(@RequestBody SavingsGoalDTO dto) {
        return ResponseEntity.ok(localService.addSavingsGoal(dto));
    }
    @GetMapping("/savings-goals")
    public ResponseEntity<List<SavingsGoalDTO>> getSavingsGoals() {
        return ResponseEntity.ok(localService.getAllSavingsGoals());
    }

    @DeleteMapping("/savings-goals/{id}")
    public ResponseEntity<Void> deleteSavingsGoal(@PathVariable Long id) {
        syncService.deleteSavingsGoalFromOracle(id);
        localService.deleteSavingsGoal(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/savings-goals/{id}/contribute")
    public ResponseEntity<SavingsGoalDTO> addContribution(
            @PathVariable Long id,
            @RequestBody Map<String, Double> payload) {

        Double amount = payload.get("amount");
        if (amount == null || amount <= 0) {
            return ResponseEntity.badRequest().build();
        }

        SavingsGoalDTO updatedGoal = localService.addContribution(id, amount);
        return ResponseEntity.ok(updatedGoal);
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, Integer>> synchronize() {
        return ResponseEntity.ok(syncService.synchronizeData());
    }

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