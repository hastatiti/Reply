package com.reply;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;

import com.reply.entity.User;
import com.reply.entity.UserAccount;
import com.reply.entity.UserPayment;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class ReplyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReplyApplication.class, args);
	}

	@Bean
	public UserRepository userRepository() {
		UserRepository userRepository = new UserRepository();
		return userRepository;
	}

	@Bean
	public UserAccountRepository userAccountRepository() {
		UserAccountRepository userAccountRepository = new UserAccountRepository();
		return userAccountRepository;
	}
	
	@Bean
	public UserPaymentRepository userPaymentRepository() {
		UserPaymentRepository userPaymentRepository = new UserPaymentRepository();
		return userPaymentRepository;
	}
	
	@Bean
	public TestRestTemplate testRestTemplate() {
	    return new TestRestTemplate();
	}
	
	public static class UserRepository {
		private List<User> users = Collections.synchronizedList(new ArrayList<User>());

		public User add(User user) {
			this.users.add(user);
			return user;
		}

		public List<User> getAll() {
			return users;
		}

		public void deleteAll() {
			users.clear();
		}
		
		public Optional<User> findByCreditCardNumber(String creditCardNumber) {
            return this.users.stream().filter(c-> c.getCreditCardNumber().equals(creditCardNumber)).findFirst();
        }
		
		public Optional<User> findByUsername(String userName) {
            return this.users.stream().filter(u-> u.getUserName().equals(userName)).findFirst();
        }
	}

	public static class UserAccountRepository {
		private List<UserAccount> userAccounts = Collections.synchronizedList(new ArrayList<UserAccount>());
		
		public UserAccount addUserAccount(UserAccount userAccount) {
			this.userAccounts.add(userAccount);
			return userAccount;
		}
		
		public List<UserAccount> getAll() {
			return userAccounts;
		}

		public void deleteAll() {
			userAccounts.clear();
		}
		public Optional<UserAccount> findByUsername(String userName) {
            return this.userAccounts.stream().filter(u-> u.getUsername().equals(userName)).findFirst();
        }
	}
	
	public static class UserPaymentRepository {
		private List<UserPayment> userPayments = Collections.synchronizedList(new ArrayList<UserPayment>());
		
		public UserPayment addUserPayment(UserPayment userPayment) {
			this.userPayments.add(userPayment);
			return userPayment;
		}
		
		public List<UserPayment> getAll() {
			return userPayments;
		}

		public void deleteAll() {
			userPayments.clear();
		}
		public Optional<UserPayment> findByCreditCardNumber(String creditCardNumber) {
            return this.userPayments.stream().filter(u-> u.getCreditCardNumber().equals(creditCardNumber)).findFirst();
        }
	}
	
}
