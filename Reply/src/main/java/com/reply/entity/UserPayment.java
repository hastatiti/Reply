package com.reply.entity;

import java.util.Objects;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;

public class UserPayment {

	@Digits(integer = 3, fraction = 0)
	private int amount;
	@Pattern(regexp = "^([0-9]{4}[\\s-]?){3}([0-9]{4})$", message = "'Credit Card Number'  is not in the required format. Must have 16 digits ")
	private String creditCardNumber;

	public UserPayment(int amount, String creditCardNumber) {
		this.amount = amount;
		this.creditCardNumber = creditCardNumber;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, creditCardNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserPayment other = (UserPayment) obj;
		return amount == other.amount && Objects.equals(creditCardNumber, other.creditCardNumber);
	}

}
