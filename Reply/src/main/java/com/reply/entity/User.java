package com.reply.entity;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

public class User {
	@NotNull
	@Pattern(regexp = "^[a-zA-Z0-9]*$", message = "'Username' is not in the required format. Must have no spaces, and only letters and numbers are allowed. ")
	private String userName;
	@NotNull
	@Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[0-9]).{8,}$", message = "'Password' is not in the required format. Must have minimum 8 characters, at least one upper case letter and a number. ")
	private String password;
	@NotNull
	@Email(message = "Email is not in the required format. ")
	private String email;
	@NotNull
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate dOB;
	@Pattern(regexp = "^([0-9]{4}[\\s-]?){3}([0-9]{4})$", message = "'Credit Card Number'  is not in the required format. Must have 16 digits ")
	private String creditCardNumber;

	public User() {

	}

	public User(String userName, String password, String email, LocalDate dOB, String creditCardNumber) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.dOB = dOB;
		this.creditCardNumber = creditCardNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getdOB() {
		return dOB;
	}

	public void setdOB(LocalDate dOB) {
		this.dOB = dOB;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	@Override
	public int hashCode() {
		return Objects.hash(creditCardNumber, dOB, email, password, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(creditCardNumber, other.creditCardNumber) && Objects.equals(dOB, other.dOB)
				&& Objects.equals(email, other.email) && Objects.equals(password, other.password)
				&& Objects.equals(userName, other.userName);
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + ", password=" + password + ", email=" + email + ", dOB=" + dOB
				+ ", creditCardNumber=" + creditCardNumber + "]";
	}

}
