package com.nibm.project.financemanager.model.sqlite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "local_savings_goals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqliteSavingsGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Double targetAmount;
    private Double currentAmount = 0.0;
    private boolean isSynced = false;

}