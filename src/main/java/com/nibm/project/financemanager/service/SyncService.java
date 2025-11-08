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
            if (syncSingleTransactionToOracle(local)) {
                local.setSynced(true); // Mark as synced
                sqliteTxRepo.save(local);
                count++;
            }
        }
        return count;
    }

    @Transactional(transactionManager = "oracleTransactionManager")
    public boolean syncSingleTransactionToOracle(SqliteTransaction local) {

        if (oracleTxRepo.existsByLocalId(local.getId())) {
            return true;
        }
        OracleTransaction central = new OracleTransaction();
        central.setLocalId(local.getId());
        central.setDescription(local.getDescription());
        central.setAmount(local.getAmount());
        central.setCategory(local.getCategory());
        central.setDate(local.getDate());
        oracleTxRepo.save(central);
        return true;
    }

    // --- Repeat for Budgets ---
    @Transactional(transactionManager = "sqliteTransactionManager")
    public int syncBudgets() {
        List<SqliteBudget> unsynced = sqliteBudgetRepo.findByIsSynced(false);
        int count = 0;
        for (SqliteBudget local : unsynced) {
            if (syncSingleBudgetToOracle(local)) {
                local.setSynced(true);
                sqliteBudgetRepo.save(local);
                count++;
            }
        }
        return count;
    }

    @Transactional(transactionManager = "oracleTransactionManager")
    public boolean syncSingleBudgetToOracle(SqliteBudget local) {
        if (oracleBudgetRepo.existsByLocalId(local.getId())) {
            return true;
        }
        OracleBudget central = new OracleBudget();
        central.setLocalId(local.getId());
        central.setCategory(local.getCategory());
        central.setAmount(local.getAmount());
        oracleBudgetRepo.save(central);
        return true;
    }

    @Transactional(transactionManager = "sqliteTransactionManager")
    public int syncSavingsGoals() { /* ... */ return 0; }
    @Transactional(transactionManager = "oracleTransactionManager")
    public boolean syncSingleGoalToOracle(SqliteSavingsGoal local) { /* ... */ return true; }
}
