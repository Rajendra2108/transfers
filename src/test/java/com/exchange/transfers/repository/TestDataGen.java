package com.exchange.transfers.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.exchange.transfers.model.Account;
import com.exchange.transfers.model.CurrencyType;

@Component
public class TestDataGen {

	private AccountRepository accountRepository;

	public TestDataGen(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public void populateSourceAccount(final Long id, final BigDecimal bal, final CurrencyType curr) {
		Account source = new Account(id, bal, curr, LocalDateTime.now());
		accountRepository.save(source);
	}

	public void populateTargetAccount(final Long id, final BigDecimal bal, final CurrencyType curr) {
		Account target = new Account(id, bal, curr, LocalDateTime.now());
		accountRepository.save(target);
	}

}
