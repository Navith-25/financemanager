package com.nibm.project.financemanager.model.oracle;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.Instant;

@Entity
@Table(name = "CENTRAL_TRANSACTIONS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OracleTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tx_seq")
    @SequenceGenerator(name = "tx_seq", sequenceName = "TRANSACTION_SEQ", allocationSize = 1)
    private Long id;

    private String description;
    private Double amount;
    private String category;
    private Instant updatedAt;

    @Column(name = "TRANSACTION_DATE")
    private LocalDate date;

    @Column(unique = true)
    private Long localId;
}