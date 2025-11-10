package com.nibm.project.financemanager.repository.sqlite;
import com.nibm.project.financemanager.model.sqlite.SqliteTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SqliteTransactionRepository extends JpaRepository<SqliteTransaction, Long> {
    List<SqliteTransaction> findByIsSynced(boolean isSynced);
}
