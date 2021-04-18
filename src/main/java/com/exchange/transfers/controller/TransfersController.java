package com.exchange.transfers.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.exchange.transfers.model.MoneyTransaction;
import com.exchange.transfers.service.TransferServiceImpl;

@RestController
public class TransfersController {

	private final TransferServiceImpl transferServer;

	@Autowired
	public TransfersController(TransferServiceImpl transferService) {
		this.transferServer = transferService;
	}

	@PostMapping("/transfers")
	public ResponseEntity<?> transferFunds(@RequestBody MoneyTransaction moneyTransaction) {
		try {
			transferServer.transfer(moneyTransaction);
		} catch (Exception e) {
			return new ResponseEntity<>("Failed to complete transaction" + e.getCause(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
