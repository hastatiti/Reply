package com.reply.entity;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import javax.validation.constraints.Digits;

public class UserAccount {

	private UUID token;
	private LocalDateTime expirationDate;
	private String username;
	@Digits(integer = 3, fraction = 0)
	private int amount;

	public UserAccount(UUID token, LocalDateTime expirationDate, String username, int amount) {
		this.token = token;
		this.expirationDate = expirationDate;
		this.username = username;
		this.amount = amount;
	}

	public UUID getToken() {
		return token;
	}

	public void setToken(UUID token) {
		this.token = token;
	}

	public LocalDateTime getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, expirationDate, token, username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAccount other = (UserAccount) obj;
		return amount == other.amount && Objects.equals(expirationDate, other.expirationDate)
				&& Objects.equals(token, other.token) && Objects.equals(username, other.username);
	}

	@Override
	public String toString() {
		return "UserAccount [token=" + token + ", expirationDate=" + expirationDate + ", username=" + username
				+ ", amount=" + amount + "]";
	}

}
