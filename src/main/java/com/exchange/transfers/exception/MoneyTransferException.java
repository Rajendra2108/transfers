package com.exchange.transfers.exception;

public class MoneyTransferException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MoneyTransferException(String message) {
		super(message);
	}

}
