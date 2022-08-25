package com.bl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bl.dto.BookDTO;
import com.bl.dto.CustomerDTO;
import com.bl.dto.LoginDTO;
import com.bl.dto.OrderDTO;
import com.bl.dto.SignUpDTO;
import com.bl.model.Book;
import com.bl.model.Customer;
import com.bl.service.CustomerService;
import com.bl.service.ICustomerService;
import com.bl.utils.JWTUtil;

@RestController
@RequestMapping("/bookstore")
public class CustomerController {

	@Autowired
	ICustomerService customerService;

	@PostMapping("/signup")
	public ResponseEntity<String> signUp(@RequestBody SignUpDTO signUpCustomer) {
		Customer customer = customerService.convertCusDtoToEntity(signUpCustomer);
		customerService.signUp(customer);
		return ResponseEntity.status(HttpStatus.OK).body("Account created Successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginCustomer) {

		String token = customerService.Login(loginCustomer);
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	
	@PutMapping("/book/cart/{bookId}")
	public ResponseEntity<Customer> addToCart(@PathVariable int bookId, @RequestHeader String token){
		return ResponseEntity.status(HttpStatus.OK).
				body(customerService.addToCart(bookId, token));
	}
	
	
	@PutMapping("/book/order")
	public ResponseEntity<String> placeOrder(@RequestBody CustomerDTO customerDTO, @RequestHeader String token){
		return ResponseEntity.status(HttpStatus.OK).
				body(customerService.placeOrder(customerDTO, token));
	}

}
