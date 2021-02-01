package com.reply;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import com.reply.ReplyApplication.UserAccountRepository;
import com.reply.ReplyApplication.UserRepository;
import com.reply.controller.UserController;
import com.reply.entity.User;
import com.reply.entity.UserAccount;
import com.reply.entity.UserPayment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserControllerTest {
		
	@Inject
	private UserController userController;
	@Inject
	private UserRepository userRepository;
	@Inject
	private UserAccountRepository userAccountRepository;
	@Inject
	private TestRestTemplate testRestTemplate;
	
	User user1 = new User("user1", "User1password", "user1@yahoo.com", LocalDate.parse("2000-01-21"), null);
	User user2 = new User("user2", "User2password", "user2@yahoo.com",LocalDate.parse("2000-01-21"), "1234567812345678");
	User user3 = new User("user3", "User3password", "user3@yahoo.com",LocalDate.parse("2000-01-21"), "1234567812345679");
	
	@Test
	public void testCreateUser() {
		userController.create(user1,null);
		UserAccount userAccount = userAccountRepository.findByUsername("user1").get();
		
		assertThat(userRepository.findByUsername("user1").get().getEmail()).isEqualTo("user1@yahoo.com");
		assertThat(userAccount.getExpirationDate()).isAfter(LocalDateTime.now().plusDays(14));
		
		ResponseEntity<String> response = testRestTemplate.getForEntity("http://localhost:8080/users", String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(200);
		
        HttpEntity<User> request = new HttpEntity<>(user2);
		ResponseEntity<String> result = testRestTemplate.postForEntity("http://localhost:8080/users", request, String.class);
		assertThat(result.getStatusCodeValue()).isEqualTo(201);
	}
	
	@Test
	public void testValidationsUserName() {
		User spaceInName = new User("user space", "User3password", "user3@yahoo.com", LocalDate.parse("2000-01-21"),
				"1234567812345679");
		ResponseEntity<String> result = testRestTemplate.postForEntity("http://localhost:8080/users", new HttpEntity<>(spaceInName),
				String.class);
		assertThat(result.getStatusCodeValue()).isEqualTo(400);
	}
	
	@Test
	public void testValidationsPassword() {
		User passwordNoUpperCase = new User("user", "userpassword1", "user3@yahoo.com", LocalDate.parse("2000-01-21"),
				"1234567812345679");
		ResponseEntity<String> result = testRestTemplate.postForEntity("http://localhost:8080/users", new HttpEntity<>(passwordNoUpperCase),
				String.class);
		assertThat(result.getStatusCodeValue()).isEqualTo(400);
		
		User passwordNoDigit = new User("user", "Userpassword", "user3@yahoo.com", LocalDate.parse("2000-01-21"),
				"1234567812345679");
		ResponseEntity<String> result1 = testRestTemplate.postForEntity("http://localhost:8080/users", new HttpEntity<>(passwordNoDigit),
				String.class);
		assertThat(result1.getStatusCodeValue()).isEqualTo(400);
	}
	
	@Test
	public void testValidationsCreditCardNumber() {
		User CreditCardNumberNot16Digits = new User("user2", "User2password", "user2@yahoo.com",LocalDate.parse("2000-01-21"), "12345678");
		ResponseEntity<String> result = testRestTemplate.postForEntity("http://localhost:8080/users", new HttpEntity<>(CreditCardNumberNot16Digits),
				String.class);
		assertThat(result.getStatusCodeValue()).isEqualTo(400);
	}
	
	@Test
	public void testValidationsUnderAge() {
		User underAge = new User("user3", "User3password", "user3@yahoo.com",LocalDate.parse("2010-01-21"), "1234567812345679");
		ResponseEntity<String> result = testRestTemplate.postForEntity("http://localhost:8080/users", new HttpEntity<>(underAge),
				String.class);
		assertThat(result.getStatusCodeValue()).isEqualTo(403);
	}
	
	@Test
	public void testValidationsUserNameUsed() {
		User user1 = new User("user1", "User1password", "user1@yahoo.com", LocalDate.parse("2000-01-21"), null);
		User user2 = new User("user1", "User2password", "user2@yahoo.com", LocalDate.parse("2001-03-24"), null);
		ResponseEntity<String> result = testRestTemplate.postForEntity("http://localhost:8080/users", new HttpEntity<>(user1),
				String.class);
		assertThat(result.getStatusCodeValue()).isEqualTo(201);
		ResponseEntity<String> result2 = testRestTemplate.postForEntity("http://localhost:8080/users", new HttpEntity<>(user2),
				String.class);
		assertThat(result2.getStatusCodeValue()).isEqualTo(409);
	}
	
	@Test
	public void testGetUsersByCreditCardNumber() {
		userController.create(user1,null); // no credit card
		userController.create(user2,null);	//	has credit card
		userController.create(user3,null);	// has credit card
		
		List<User> allUsers = userController.getUsers(null);
		assertThat(allUsers.size()).isEqualTo(3);
		List<User> withCreditCard = userController.getUsers(true);
		assertThat(withCreditCard.size()).isEqualTo(2);
		List<User> withoutCreditCard = userController.getUsers(false);
		assertThat(withoutCreditCard.size()).isEqualTo(1);
	}
	
	@Test
	public void testUnregisteredCreditCardNumber() {
		HttpEntity<User> request = new HttpEntity<>(user2);
		ResponseEntity<String> result = testRestTemplate.postForEntity("http://localhost:8080/users", request, String.class);
		assertThat(result.getStatusCodeValue()).isEqualTo(201);
		
		UserPayment userPayment = new UserPayment(100, "1234567812345678");
		
		ResponseEntity<String> result2 = testRestTemplate.postForEntity("http://localhost:8080/payments", new HttpEntity<>(userPayment),
				String.class);
		assertThat(result2.getStatusCodeValue()).isEqualTo(201);
		
		UserPayment userPayment2 = new UserPayment(100, "9234567812345679");
		
		ResponseEntity<String> result3 = testRestTemplate.postForEntity("http://localhost:8080/payments", new HttpEntity<>(userPayment2),
				String.class);
		assertThat(result3.getStatusCodeValue()).isEqualTo(404);
	}
	
}
