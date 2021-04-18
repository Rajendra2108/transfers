package com.exchange.transfers.controller;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.net.URI;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.exchange.transfers.TransfersApplication;
import com.exchange.transfers.model.CurrencyType;
import com.exchange.transfers.model.MoneyTransaction;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransfersApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransfersControllerTest {

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = new HttpHeaders();

	@Before
	public void setUp() {
		headers.setContentType(MediaType.APPLICATION_JSON);
	}

	@Test
	public void validAccountsSufficientFundsTransferSuccess() {
		HttpEntity<MoneyTransaction> entity = new HttpEntity<>(validTransaction(), headers);

		ResponseEntity<?> response = restTemplate.exchange(createURLWithPort("/transfers"), HttpMethod.POST, entity,
				MoneyTransaction.class);
		assertEquals(200, response.getStatusCode().value());
	}

	@Test
	public void validAccountsInsufficientFundsTransferFailure() {
		HttpEntity<MoneyTransaction> entity = new HttpEntity<>(insufficientFundsTransaction(), headers);

		ResponseEntity<?> response = restTemplate.exchange(createURLWithPort("/transfers"), HttpMethod.POST, entity,
				String.class);
		assertEquals(400, response.getStatusCode().value());
	}

	@Test
	public void invalidAccountsTransferFailure() {
		HttpEntity<MoneyTransaction> entity = new HttpEntity<>(invalidAccounts(), headers);

		ResponseEntity<?> response = restTemplate.exchange(createURLWithPort("/transfers"), HttpMethod.POST, entity,
				String.class);
		assertEquals(400, response.getStatusCode().value());
	}
	
	@Test
	public void notSupportedCurrencyTransferFailure() {
		HttpEntity<MoneyTransaction> entity = new HttpEntity<>(notSupportedCurrencyType(), headers);

		ResponseEntity<?> response = restTemplate.exchange(createURLWithPort("/transfers"), HttpMethod.POST, entity,
				String.class);
		assertEquals(400, response.getStatusCode().value());
	}
	
	private MoneyTransaction validTransaction() {
		return new MoneyTransaction(10L, 1L, 2L, BigDecimal.valueOf(30), CurrencyType.GBP);
	}

	private MoneyTransaction insufficientFundsTransaction() {
		return new MoneyTransaction(11L, 1L, 2L, BigDecimal.valueOf(1130), CurrencyType.GBP);
	}

	private MoneyTransaction invalidAccounts() {
		return new MoneyTransaction(10L, 199L, 2L, BigDecimal.valueOf(30), CurrencyType.GBP);
	}
	
	private MoneyTransaction notSupportedCurrencyType() {
		return new MoneyTransaction(10L, 1L, 2L, BigDecimal.valueOf(30), CurrencyType.EUR);
	}
	
	private URI createURLWithPort(String uri) {
		return URI.create("http://localhost:" + port + uri);
	}
}
