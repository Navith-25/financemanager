package com.nibm.project.financemanager.model.oracle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import java.time.Instant;

@Entity
@Table(name = "CENTRAL_SAVINGS_GOALS")
@Check(constraints = "current_amount <= target_amount")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OracleSavingsGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "goal_seq")
    @SequenceGenerator(name = "goal_seq", sequenceName = "GOAL_SEQ", allocationSize = 1)
    private Long id;
    private String name;

    @Column(name = "UPDATED_AT")
    private Instant updatedAt;

    @Column(nullable = false)
    private Double targetAmount;

    @Column(columnDefinition = "NUMBER(10,2) DEFAULT 0.0")
    private Double currentAmount;

    @Column(name = "LOCAL_ID", unique = true)
    private Long localId;
}