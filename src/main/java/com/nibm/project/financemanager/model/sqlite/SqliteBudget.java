package com.nibm.project.financemanager.model.sqlite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Entity
@Table(name = "local_budgets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqliteBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String category;
    @Column(nullable = false)
    private Double amount;
    private boolean isSynced = false;
    private Instant updatedAt;

}