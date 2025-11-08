package com.nibm.project.financemanager.service;

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
            syncSingleTransactionToOracle(local);
            local.setSynced(true);
            sqliteTxRepo.save(local);
            count++;
        }
        return count;
    }

    @Transactional(transactionManager = "oracleTransactionManager")
    public void syncSingleTransactionToOracle(SqliteTransaction local) {
        oracleTxRepo.syncTransaction(
                local.getId(),
                local.getDescription(),
                local.getAmount(),
                local.getCategory(),
                local.getDate(),
                local.getUpdatedAt()
        );
    }
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
        oracleBudgetRepo.syncBudget(
                local.getId(),
                local.getCategory(),
                local.getAmount(),
                local.getUpdatedAt()
        );
    }
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
        oracleGoalRepo.syncSavingsGoal(
                local.getId(),
                local.getName(),
                local.getTargetAmount(),
                local.getCurrentAmount(),
                local.getUpdatedAt()
        );
    }

    @Transactional(transactionManager = "oracleTransactionManager")
    public void deleteTransactionFromOracle(Long localId) {
        if (oracleTxRepo.existsByLocalId(localId)) {
            oracleTxRepo.deleteByLocalId(localId);
        }
    }

    @Transactional(transactionManager = "oracleTransactionManager")
    public void deleteBudgetFromOracle(Long localId) {
        if (oracleBudgetRepo.existsByLocalId(localId)) {
            oracleBudgetRepo.deleteByLocalId(localId);
        }
    }

    @Transactional(transactionManager = "oracleTransactionManager")
    public void deleteSavingsGoalFromOracle(Long localId) {
        if (oracleGoalRepo.existsByLocalId(localId)) {
            oracleGoalRepo.deleteByLocalId(localId);
        }
    }
}