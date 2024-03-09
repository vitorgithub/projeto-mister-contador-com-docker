package com.vitorgithub.pdfextractapi.infrastructure.repository;

import com.vitorgithub.pdfextractapi.domain.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByDateAndDocumentNumberAndAmount(LocalDate date, String documentNumber, BigDecimal amount);
}
