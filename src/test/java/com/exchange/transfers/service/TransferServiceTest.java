package com.exchange.transfers.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.exchange.transfers.TransfersApplication;
import com.exchange.transfers.exception.AccountNotFoundException;
import com.exchange.transfers.exception.InsufficientFundsException;
import com.exchange.transfers.exception.MoneyTransferException;
import com.exchange.transfers.model.Account;
import com.exchange.transfers.model.CurrencyType;
import com.exchange.transfers.model.MoneyTransaction;
import com.exchange.transfers.repository.AccountRepository;
import com.exchange.transfers.repository.TestDataGen;
import com.exchange.transfers.repository.TransactionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransfersApplication.class)
public class TransferServiceTest {

	private TestDataGen testDataGen;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TransferService transferService;

	@BeforeEach
	public void setUp() {
		testDataGen = new TestDataGen(accountRepository);

	}

	@Test
	public void validSourceAndTargetFundsExistsTransferSuccess() {

		final Long sourceAccountId = 101L;
		final Long targetAccountId = 201L;
		testDataGen.populateSourceAccount(sourceAccountId, BigDecimal.valueOf(250.50), CurrencyType.GBP);
		testDataGen.populateTargetAccount(targetAccountId, BigDecimal.valueOf(60.50), CurrencyType.GBP);
		MoneyTransaction mt = toMoneyTransaction();
		transferService.transfer(mt);

		Account sourceAccount = accountRepository.findById(sourceAccountId).get();
		Account targetAccount = accountRepository.findById(targetAccountId).get();
		assertEquals(BigDecimal.valueOf(150.50), sourceAccount.getBalance());
		assertEquals(BigDecimal.valueOf(160.50), targetAccount.getBalance());

		MoneyTransaction moneyTransaction = transactionRepository.findById(11L).get();
		assertTrue(BigDecimal.valueOf(100.00).compareTo(moneyTransaction.getAmount()) == 0);
	}

	@Test
	public void validSourceAndTargetInsufficientFundsTransferError() {

		final Long sourceAccountId = 101L;
		final Long targetAccountId = 201L;
		testDataGen.populateSourceAccount(sourceAccountId, BigDecimal.valueOf(59.90), CurrencyType.GBP);
		testDataGen.populateTargetAccount(targetAccountId, BigDecimal.valueOf(160.50), CurrencyType.GBP);
		MoneyTransaction mt = toMoneyTransaction();

		assertThrows(InsufficientFundsException.class, () -> transferService.transfer(mt));

		Account sourceAccount = accountRepository.findById(sourceAccountId).get();
		Account targetAccount = accountRepository.findById(targetAccountId).get();
		assertEquals(BigDecimal.valueOf(59.90), sourceAccount.getBalance());
		assertEquals(BigDecimal.valueOf(160.50), targetAccount.getBalance());
	}

	@Test
	public void validSourceAndTargetAccountsIncompatibleCurrencyTransferError() {

		final Long sourceAccountId = 101L;
		final Long targetAccountId = 201L;
		testDataGen.populateSourceAccount(sourceAccountId, BigDecimal.valueOf(159.90), CurrencyType.EUR);
		testDataGen.populateTargetAccount(targetAccountId, BigDecimal.valueOf(160.50), CurrencyType.GBP);
		MoneyTransaction mt = toMoneyTransaction();

		assertThrows(MoneyTransferException.class, () -> transferService.transfer(mt));

		Account sourceAccount = accountRepository.findById(sourceAccountId).get();
		Account targetAccount = accountRepository.findById(targetAccountId).get();
		assertEquals(BigDecimal.valueOf(159.90), sourceAccount.getBalance());
		assertEquals(BigDecimal.valueOf(160.50), targetAccount.getBalance());
	}

	@Test
	public void validAccountsTransferBetweenSameAccountError() {

		final Long sourceAccountId = 101L;
		testDataGen.populateSourceAccount(sourceAccountId, BigDecimal.valueOf(159.90), CurrencyType.EUR);
		MoneyTransaction mt = sameAccountTransaction();

		assertThrows(MoneyTransferException.class, () -> transferService.transfer(mt));

		Account sourceAccount = accountRepository.findById(sourceAccountId).get();
		assertEquals(BigDecimal.valueOf(159.90), sourceAccount.getBalance());
	}

	@Test
	public void invalidTargetAccountTransferError() {

		final Long sourceAccountId = 101L;
		final Long targetAccountId = 201L;
		testDataGen.populateSourceAccount(sourceAccountId, BigDecimal.valueOf(159.90), CurrencyType.EUR);
		testDataGen.populateTargetAccount(targetAccountId, BigDecimal.valueOf(160.50), CurrencyType.GBP);
		MoneyTransaction mt = invalidTargetAccountTransaction();

		assertThrows(AccountNotFoundException.class, () -> transferService.transfer(mt));

		Account sourceAccount = accountRepository.findById(sourceAccountId).get();
		Account targetAccount = accountRepository.findById(targetAccountId).get();
		assertEquals(BigDecimal.valueOf(159.90), sourceAccount.getBalance());
		assertEquals(BigDecimal.valueOf(160.50), targetAccount.getBalance());
	}

	
	@Test
	public void invalidSourceAccountTransferError() {

		final Long sourceAccountId = 101L;
		final Long targetAccountId = 201L;
		testDataGen.populateSourceAccount(sourceAccountId, BigDecimal.valueOf(159.90), CurrencyType.EUR);
		testDataGen.populateTargetAccount(targetAccountId, BigDecimal.valueOf(160.50), CurrencyType.GBP);
		MoneyTransaction mt = invalidSourceAccountTransaction();

		assertThrows(AccountNotFoundException.class, () -> transferService.transfer(mt));

		Account sourceAccount = accountRepository.findById(sourceAccountId).get();
		Account targetAccount = accountRepository.findById(targetAccountId).get();
		assertEquals(BigDecimal.valueOf(159.90), sourceAccount.getBalance());
		assertEquals(BigDecimal.valueOf(160.50), targetAccount.getBalance());
	}

	private MoneyTransaction toMoneyTransaction() {
		return new MoneyTransaction(11L, 101L, 201L, BigDecimal.valueOf(100), CurrencyType.GBP);
	}

	private MoneyTransaction sameAccountTransaction() {
		return new MoneyTransaction(11L, 101L, 101L, BigDecimal.valueOf(100), CurrencyType.GBP);
	}

	private MoneyTransaction invalidTargetAccountTransaction() {
		return new MoneyTransaction(11L, 101L, 301L, BigDecimal.valueOf(100), CurrencyType.GBP);
	}
	
	private MoneyTransaction invalidSourceAccountTransaction() {
		return new MoneyTransaction(11L, 901L, 201L, BigDecimal.valueOf(100), CurrencyType.GBP);
	}

}
