package com.nibm.project.financemanager.repository.oracle;

import com.nibm.project.financemanager.model.oracle.OracleBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import java.time.Instant;

public interface OracleBudgetRepository extends JpaRepository<OracleBudget, Long> {
    boolean existsByLocalId(Long localId);

    @Procedure(procedureName = "sp_sync_budget")
    void syncBudget(
            @Param("p_local_id") Long localId,
            @Param("p_category") String category,
            @Param("p_amount") Double amount,
            @Param("p_updated_at") Instant updatedAt
    );
}