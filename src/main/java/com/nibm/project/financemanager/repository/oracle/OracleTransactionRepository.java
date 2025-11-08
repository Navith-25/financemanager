package com.nibm.project.financemanager.repository.oracle;
import com.nibm.project.financemanager.model.oracle.OracleTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.time.LocalDate;

public interface OracleTransactionRepository extends JpaRepository<OracleTransaction, Long> {

    boolean existsByLocalId(Long localId);

    @Procedure(procedureName = "sp_sync_transaction")
    void syncTransaction(
            @Param("p_local_id") Long localId,
            @Param("p_description") String description,
            @Param("p_amount") Double amount,
            @Param("p_category") String category,
            @Param("p_date") LocalDate date,
            @Param("p_updated_at") Instant updatedAt
    );

    void deleteByLocalId(Long localId);
}