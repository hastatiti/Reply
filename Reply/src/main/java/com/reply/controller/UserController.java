package com.reply.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.reply.ReplyApplication.UserAccountRepository;
import com.reply.ReplyApplication.UserPaymentRepository;
import com.reply.ReplyApplication.UserRepository;
import com.reply.entity.User;
import com.reply.entity.UserAccount;
import com.reply.entity.UserPayment;

@RestController
@RequestMapping
public class UserController {

	// all repositories are injected via 'ReplyApplication' as List of entities, no  Databases/in-memory Databases are used
	@Inject
	private UserRepository userRepository;
	@Inject
	private UserAccountRepository userAccountRepository;
	@Inject
	private UserPaymentRepository userPaymentRepository;
	
	@PostMapping("/users")
//	Spring validates with the JSR-303 provider (anytime @Valid is encountered), 
//	Spring needs 'BindingResult' object to expose the potential errors.
	public ResponseEntity<String> create(@Valid @RequestBody final User user, BindingResult bindingResult) {
		if (bindingResult != null && bindingResult.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		int age = Period.between(user.getdOB(), LocalDate.now()).getYears();
		if (age < 18) {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		if (userRepository.findByUsername(user.getUserName()).isPresent()) {
			return new ResponseEntity<>("user already exists ", HttpStatus.CONFLICT);
		}
		userRepository.add(user);
		// user given a token with 15 days expiry date
		UserAccount userAccount = new UserAccount(UUID.randomUUID(), LocalDateTime.now().plusDays(15),
				user.getUserName(), 0);
		userAccountRepository.addUserAccount(userAccount);
		return new ResponseEntity<>("user created ", HttpStatus.CREATED);
	}

	@GetMapping("/users")
	public List<User> getUsers(@RequestParam(required = false) Boolean flag) {
		if (flag != null && flag) {
			List<User> users = userRepository.getAll();
			List<User> withCreditCard = users.stream().filter(u -> u.getCreditCardNumber() != null)
					.collect(Collectors.toList());
			return withCreditCard;
		}
		if (flag != null && !flag) {
			List<User> users = userRepository.getAll();
			List<User> withoutCreditCard = users.stream().filter(u -> u.getCreditCardNumber() == null)
					.collect(Collectors.toList());
			return withoutCreditCard;
		}
		return userRepository.getAll();

	}

	@PostMapping("/payments")
	public ResponseEntity<String> makePayment(@Valid @RequestBody final UserPayment userPayment,BindingResult bindingResult){
		if(bindingResult != null && bindingResult.hasErrors()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if(userRepository.findByCreditCardNumber(userPayment.getCreditCardNumber()).isEmpty()) {
			return new ResponseEntity<>("creditCardNumber has not been registered to any user ", HttpStatus.NOT_FOUND);
		}
		userPaymentRepository.addUserPayment(userPayment);
		User user = userRepository.findByCreditCardNumber(userPayment.getCreditCardNumber()).orElseThrow();
		UserAccount userAccount =userAccountRepository.findByUsername(user.getUserName()).orElseThrow();
//		here user will/may be given a new token depending on payment received, via some paymentServiceImpl(here it is omitted for simplicity)
//		userAccount.setExpirationDate(expirationDate);
		userAccount.setAmount(userPayment.getAmount());
		return new ResponseEntity<>("payment received ", HttpStatus.CREATED);
	}

}
