package com.nibm.project.financemanager.model.oracle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;


@Entity
@Table(name = "CENTRAL_BUDGETS",
        uniqueConstraints = @UniqueConstraint(columnNames = {"category"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OracleBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "budget_seq")
    @SequenceGenerator(name = "budget_seq", sequenceName = "BUDGET_SEQ", allocationSize = 1)
    private Long id;
    private String category;

    @Column(name = "UPDATED_AT")
    private Instant updatedAt;

    @Column(nullable = false)
    private Double amount;

    @Column(name = "LOCAL_ID", unique = true)
    private Long localId;

}
