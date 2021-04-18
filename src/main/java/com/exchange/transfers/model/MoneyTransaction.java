package com.exchange.transfers.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "transaction")
public class MoneyTransaction {

	@Id
	private Long id;
	private Long sourceAccountId;
	private Long targetAccountId;
	@Column(name = "amount", columnDefinition = "NUMBER")
	private BigDecimal amount;
	@Enumerated(EnumType.STRING)
	private CurrencyType currency;

	public MoneyTransaction() {
		super();
	}

	public MoneyTransaction(Long id, Long sourceAccountId, Long targetAccountId, BigDecimal amount,
			CurrencyType currency) {
		super();
		this.id = id;
		this.sourceAccountId = sourceAccountId;
		this.targetAccountId = targetAccountId;
		this.amount = amount;
		this.currency = currency;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSourceAccountId() {
		return sourceAccountId;
	}

	public void setSourceAccountId(Long sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}

	public Long getTargetAccountId() {
		return targetAccountId;
	}

	public void setTargetAccountId(Long targetAccountId) {
		this.targetAccountId = targetAccountId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public CurrencyType getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyType currency) {
		this.currency = currency;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((sourceAccountId == null) ? 0 : sourceAccountId.hashCode());
		result = prime * result + ((targetAccountId == null) ? 0 : targetAccountId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MoneyTransaction other = (MoneyTransaction) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (currency != other.currency)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (sourceAccountId == null) {
			if (other.sourceAccountId != null)
				return false;
		} else if (!sourceAccountId.equals(other.sourceAccountId))
			return false;
		if (targetAccountId == null) {
			if (other.targetAccountId != null)
				return false;
		} else if (!targetAccountId.equals(other.targetAccountId))
			return false;
		return true;
	}

}
