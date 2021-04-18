package com.exchange.transfers.service;

import com.exchange.transfers.model.MoneyTransaction;

public interface TransferService {

	public void transfer(MoneyTransaction mt);
}
