package com.exchange.transfers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exchange.transfers.model.MoneyTransaction;

@Repository
public interface TransactionRepository extends JpaRepository<MoneyTransaction, Long> {

}
