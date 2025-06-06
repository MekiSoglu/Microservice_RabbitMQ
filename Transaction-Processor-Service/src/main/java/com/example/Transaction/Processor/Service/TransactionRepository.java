package com.example.Transaction.Processor.Service;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<TransactionLog,Long> {
}
