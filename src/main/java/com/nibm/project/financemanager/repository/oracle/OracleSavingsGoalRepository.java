package com.nibm.project.financemanager.repository.oracle;

import com.nibm.project.financemanager.model.oracle.OracleSavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OracleSavingsGoalRepository extends JpaRepository<OracleSavingsGoal, Long> {
    boolean existsByLocalId(Long localId);
}
