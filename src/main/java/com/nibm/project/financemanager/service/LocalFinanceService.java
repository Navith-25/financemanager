package com.nibm.project.financemanager.service;

import com.nibm.project.financemanager.dto.BudgetDTO;
import com.nibm.project.financemanager.dto.SavingsGoalDTO;
import com.nibm.project.financemanager.dto.TransactionDTO;
import com.nibm.project.financemanager.model.sqlite.SqliteBudget;
import com.nibm.project.financemanager.model.sqlite.SqliteSavingsGoal;
import com.nibm.project.financemanager.model.sqlite.SqliteTransaction;
import com.nibm.project.financemanager.repository.sqlite.SqliteBudgetRepository;
import com.nibm.project.financemanager.repository.sqlite.SqliteSavingsGoalRepository;
import com.nibm.project.financemanager.repository.sqlite.SqliteTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(transactionManager = "sqliteTransactionManager")
public class LocalFinanceService {

    @Autowired private SqliteTransactionRepository txRepo;
    @Autowired private SqliteBudgetRepository budgetRepo;
    @Autowired private SqliteSavingsGoalRepository goalRepo;

    // --- Transaction Methods ---
    public TransactionDTO addTransaction(TransactionDTO dto) {
        SqliteTransaction tx = new SqliteTransaction();
        tx.setDescription(dto.description());
        tx.setAmount(dto.amount());
        tx.setCategory(dto.category());
        tx.setDate(dto.date());
        tx.setSynced(false); // Mark as new
        tx = txRepo.save(tx);
        return toDto(tx);
    }
    public List<TransactionDTO> getAllTransactions() {
        return txRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // --- Budget Methods ---
    public BudgetDTO setBudget(BudgetDTO dto) {
        // Find existing budget or create new
        SqliteBudget budget = budgetRepo.findByCategory(dto.category());
        if (budget == null) {
            budget = new SqliteBudget();
            budget.setCategory(dto.category());
        }
        budget.setAmount(dto.amount());
        budget.setSynced(false); // Mark for sync
        budget = budgetRepo.save(budget);
        return toDto(budget);
    }
    public List<BudgetDTO> getAllBudgets() {
        return budgetRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // --- Savings Goal Methods ---
    public SavingsGoalDTO addSavingsGoal(SavingsGoalDTO dto) {
        SqliteSavingsGoal goal = new SqliteSavingsGoal();
        goal.setName(dto.name());
        goal.setTargetAmount(dto.targetAmount());
        goal.setCurrentAmount(0.0);
        goal.setSynced(false);
        goal = goalRepo.save(goal);
        return toDto(goal);
    }
    public List<SavingsGoalDTO> getAllSavingsGoals() {
        return goalRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    // --- Private DTO Mappers ---
    private TransactionDTO toDto(SqliteTransaction tx) {
        return new TransactionDTO(tx.getId(), tx.getDescription(), tx.getAmount(), tx.getCategory(), tx.getDate(), tx.isSynced());
    }
    private BudgetDTO toDto(SqliteBudget b) {
        return new BudgetDTO(b.getId(), b.getCategory(), b.getAmount(), b.isSynced());
    }
    private SavingsGoalDTO toDto(SqliteSavingsGoal g) {
        return new SavingsGoalDTO(g.getId(), g.getName(), g.getTargetAmount(), g.getCurrentAmount(), g.isSynced());
    }
}
