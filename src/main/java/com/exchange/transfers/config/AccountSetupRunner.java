package com.exchange.transfers.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.exchange.transfers.model.Account;
import com.exchange.transfers.model.CurrencyType;
import com.exchange.transfers.repository.AccountRepository;

@Component
public class AccountSetupRunner implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(AccountSetupRunner.class);
	private AccountRepository accountRepository;

	public AccountSetupRunner(AccountRepository accountRepository) {
		super();
		this.accountRepository = accountRepository;
	}

	public void run(String... args) throws Exception {

		log.info("Setting up initial account data");
		accountRepository.save(new Account(1L, BigDecimal.valueOf(71.50), CurrencyType.GBP, LocalDateTime.now()));
		accountRepository.save(new Account(2L, BigDecimal.valueOf(450.50), CurrencyType.GBP, LocalDateTime.now()));

	}

}
