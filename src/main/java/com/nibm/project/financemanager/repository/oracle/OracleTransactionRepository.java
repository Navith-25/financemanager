package com.nibm.project.financemanager.repository.oracle;
import com.nibm.project.financemanager.model.oracle.OracleTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OracleTransactionRepository extends JpaRepository<OracleTransaction, Long> {

    boolean existsByLocalId(Long localId);
}