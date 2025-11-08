package com.nibm.project.financemanager.repository.sqlite;

import com.nibm.project.financemanager.model.sqlite.SqliteSavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SqliteSavingsGoalRepository extends JpaRepository<SqliteSavingsGoal, Long> {
    List<SqliteSavingsGoal> findByIsSynced(boolean isSynced);
}