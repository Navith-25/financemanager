package com.nibm.project.financemanager.model.oracle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    @Column(nullable = false)
    private Double amount;
    @Column(unique = true)
    private Long localId;

}
