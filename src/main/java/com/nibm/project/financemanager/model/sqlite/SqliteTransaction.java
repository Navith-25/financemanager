package com.nibm.project.financemanager.model.sqlite;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

import java.time.LocalDate;

@Entity
@Table(name = "local_transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SqliteTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Double amount;
    private String category;
    private LocalDate date;
    private boolean isSynced = false;
    private Instant updatedAt;

}




