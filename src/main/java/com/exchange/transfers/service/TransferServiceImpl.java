package com.exchange.transfers.service;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.exchange.transfers.exception.AccountNotFoundException;
import com.exchange.transfers.exception.InsufficientFundsException;
import com.exchange.transfers.exception.MoneyTransferException;
import com.exchange.transfers.model.Account;
import com.exchange.transfers.model.MoneyTransaction;
import com.exchange.transfers.repository.AccountRepository;
import com.exchange.transfers.repository.TransactionRepository;

@Component
public class TransferServiceImpl implements TransferService {

	private static final Logger log = LoggerFactory.getLogger(TransferServiceImpl.class);

	private AccountRepository accountRepository;

	private TransactionRepository transactionRepository;

	@Autowired
	public TransferServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	public void transfer(MoneyTransaction mt) {
		log.info("Initiating transfer");
		Long sourceAccountId = mt.getSourceAccountId();
		Long targetAccountId = mt.getTargetAccountId();

		if (sourceAccountId.equals(targetAccountId)) {
			throw new MoneyTransferException("Transactions not allowed within same Account");
		}

		Account sourceAccount = accountRepository.findById(sourceAccountId)
				.orElseThrow(() -> new AccountNotFoundException("Source Account not found"));

		Account targetAccount = accountRepository.findById(targetAccountId)
				.orElseThrow(() -> new AccountNotFoundException("Target Account not found"));

		if (!sourceAccount.getCurrency().equals(targetAccount.getCurrency()) || !mt.getCurrency().equals(sourceAccount.getCurrency())) {
			throw new MoneyTransferException("Transactions not allowed between different currencies");
		}

		if (sourceAccount.getBalance().compareTo(mt.getAmount()) < 0) {
			throw new InsufficientFundsException("Transaction not allowed, insufficient funds");
		}

		synchronized (sourceAccount) {
			synchronized (targetAccount) {

				BigDecimal sourceBalance = sourceAccount.getBalance().subtract(mt.getAmount());
				BigDecimal targetBalance = targetAccount.getBalance().add(mt.getAmount());
				sourceAccount.setBalance(sourceBalance);
				targetAccount.setBalance(targetBalance);
				accountRepository.save(sourceAccount);
				accountRepository.save(targetAccount);
				transactionRepository.save(mt);
			}

		}

		log.info("Transaction successful");

	}

}
