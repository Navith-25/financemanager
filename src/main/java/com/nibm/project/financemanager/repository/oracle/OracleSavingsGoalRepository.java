package com.nibm.project.financemanager.repository.oracle;

import com.nibm.project.financemanager.model.oracle.OracleSavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import java.time.Instant;

public interface OracleSavingsGoalRepository extends JpaRepository<OracleSavingsGoal, Long> {
    boolean existsByLocalId(Long localId);

    @Procedure(procedureName = "sp_sync_savings_goal")
    void syncSavingsGoal(
            @Param("p_local_id") Long localId,
            @Param("p_name") String name,
            @Param("p_target_amount") Double targetAmount,
            @Param("p_current_amount") Double currentAmount,
            @Param("p_updated_at") Instant updatedAt
    );

    void deleteByLocalId(Long localId);
}