package com.nibm.project.financemanager.repository.oracle;

import com.nibm.project.financemanager.model.oracle.OracleBudget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OracleBudgetRepository extends JpaRepository<OracleBudget, Long> {
    boolean existsByLocalId(Long localId);
}
