package com.exchange.transfers.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exchange.transfers.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
