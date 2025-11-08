package com.nibm.project.financemanager.service;

import com.nibm.project.financemanager.model.oracle.OracleBudget;
import com.nibm.project.financemanager.model.oracle.OracleTransaction;
import com.nibm.project.financemanager.model.sqlite.SqliteBudget;
import com.nibm.project.financemanager.model.sqlite.SqliteSavingsGoal;
import com.nibm.project.financemanager.model.sqlite.SqliteTransaction;
import com.nibm.project.financemanager.repository.oracle.OracleBudgetRepository;
import com.nibm.project.financemanager.repository.oracle.OracleSavingsGoalRepository;
import com.nibm.project.financemanager.repository.oracle.OracleTransactionRepository;
import com.nibm.project.financemanager.repository.sqlite.SqliteBudgetRepository;
import com.nibm.project.financemanager.repository.sqlite.SqliteSavingsGoalRepository;
import com.nibm.project.financemanager.repository.sqlite.SqliteTransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SyncService {

    // --- SQLite Repos (Primary) ---
    @Autowired private SqliteTransactionRepository sqliteTxRepo;
    @Autowired private SqliteBudgetRepository sqliteBudgetRepo;
    @Autowired private SqliteSavingsGoalRepository sqliteGoalRepo;

    // --- Oracle Repos (Central) ---
    @Autowired private OracleTransactionRepository oracleTxRepo;
    @Autowired private OracleBudgetRepository oracleBudgetRepo;
    @Autowired private OracleSavingsGoalRepository oracleGoalRepo;

    public Map<String, Integer> synchronizeData() {
        int syncedTransactions = syncTransactions();
        int syncedBudgets = syncBudgets();
        int syncedGoals = syncSavingsGoals();

        return Map.of(
                "transactions", syncedTransactions,
                "budgets", syncedBudgets,
                "goals", syncedGoals
        );
    }

    @Transactional(transactionManager = "sqliteTransactionManager")
    public int syncTransactions() {
        List<SqliteTransaction> unsynced = sqliteTxRepo.findByIsSynced(false);
        int count = 0;
        for (SqliteTransaction local : unsynced) {
            // Use the Oracle procedure to sync
            syncSingleTransactionToOracle(local);

            // Mark as synced in SQLite
            local.setSynced(true);
            sqliteTxRepo.save(local);
            count++;
        }
        return count;
    }

    @Transactional(transactionManager = "oracleTransactionManager")
    public void syncSingleTransactionToOracle(SqliteTransaction local) {
        // This now calls our PL/SQL MERGE procedure
        oracleTxRepo.syncTransaction(
                local.getId(),
                local.getDescription(),
                local.getAmount(),
                local.getCategory(),
                local.getDate(),
                local.getUpdatedAt()
        );
    }

    // --- Repeat for Budgets ---
    @Transactional(transactionManager = "sqliteTransactionManager")
    public int syncBudgets() {
        List<SqliteBudget> unsynced = sqliteBudgetRepo.findByIsSynced(false);
        int count = 0;
        for (SqliteBudget local : unsynced) {
            syncSingleBudgetToOracle(local);
            local.setSynced(true);
            sqliteBudgetRepo.save(local);
            count++;
        }
        return count;
    }

    @Transactional(transactionManager = "oracleTransactionManager")
    public void syncSingleBudgetToOracle(SqliteBudget local) {
        // This now calls our PL/SQL MERGE procedure
        oracleBudgetRepo.syncBudget(
                local.getId(),
                local.getCategory(),
                local.getAmount(),
                local.getUpdatedAt()
        );
    }

    // --- Repeat for Savings Goals (This was missing from your file) ---
    @Transactional(transactionManager = "sqliteTransactionManager")
    public int syncSavingsGoals() {
        List<SqliteSavingsGoal> unsynced = sqliteGoalRepo.findByIsSynced(false);
        int count = 0;
        for (SqliteSavingsGoal local : unsynced) {
            syncSingleGoalToOracle(local);
            local.setSynced(true);
            sqliteGoalRepo.save(local);
            count++;
        }
        return count;
    }

    @Transactional(transactionManager = "oracleTransactionManager")
    public void syncSingleGoalToOracle(SqliteSavingsGoal local) {
        // This now calls our PL/SQL MERGE procedure
        oracleGoalRepo.syncSavingsGoal(
                local.getId(),
                local.getName(),
                local.getTargetAmount(),
                local.getCurrentAmount(),
                local.getUpdatedAt()
        );
    }
}