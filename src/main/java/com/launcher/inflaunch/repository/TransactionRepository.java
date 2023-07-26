package com.launcher.inflaunch.repository;

import com.launcher.inflaunch.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}