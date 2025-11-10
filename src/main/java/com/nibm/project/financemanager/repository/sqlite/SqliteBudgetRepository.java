package com.nibm.project.financemanager.repository.sqlite;
import com.nibm.project.financemanager.model.sqlite.SqliteBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SqliteBudgetRepository extends JpaRepository<SqliteBudget, Long> {
    List<SqliteBudget> findByIsSynced(boolean isSynced);
    SqliteBudget findByCategory(String category);
}