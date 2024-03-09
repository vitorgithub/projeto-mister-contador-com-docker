package com.vitorgithub.pdfextractapi.domain.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 50, name = "document_number")
    private String documentNumber;

    @Column(nullable = false, length = 255, name = "history")
    private String history;

    @Column(nullable = false, name = "amount")
    private BigDecimal amount;

    @Column(nullable = false, name = "balance")
    private BigDecimal balance;
}