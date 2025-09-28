package com.mirexelle.bankappTesda.repository;

import com.mirexelle.bankappTesda.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findByAccountId (Long accountId);
}
