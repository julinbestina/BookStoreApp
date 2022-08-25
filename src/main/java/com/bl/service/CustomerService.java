package com.bl.service;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bl.Exception.CustomerException;
import com.bl.dto.CustomerDTO;
import com.bl.dto.LoginDTO;
import com.bl.dto.OrderDTO;
import com.bl.dto.SignUpDTO;
import com.bl.model.Book;
import com.bl.model.Customer;
import com.bl.repository.BookRepository;
import com.bl.repository.CustomerRepository;
import com.bl.utils.EmailSenderService;
import com.bl.utils.JWTUtil;

@Service
public class CustomerService implements ICustomerService {

	@Autowired
	CustomerRepository customerRepo;

	@Autowired
	BookRepository bookRepo;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	EmailSenderService emailSenderService;

	@Autowired
	private AmqpTemplate amqpTemplate;

	public Customer convertCusDtoToEntity(SignUpDTO loginCustomer) {
		Customer customer = new Customer();
		customer = modelMapper.map(loginCustomer, Customer.class);
		return customer;
	}

	public Customer signUp(Customer customer) {

		customerRepo.findByEmailId(customer.getEmailId()).ifPresent((existingCus) -> {
			throw new CustomerException("Account already exists");
		});
		return customerRepo.save(customer);
	}

	public String Login(LoginDTO loginCustomer) {

		customerRepo.findByEmailIdAndByPassword(loginCustomer.getEmailId(), loginCustomer.getPassword())
				.orElseThrow(() -> new CustomerException("Incorrect email or password"));
		return jwtUtil.generateJWT(loginCustomer);
	}

	public Customer addToCart(int bookId, String token) {
		String emailId = jwtUtil.verify(token);
		Customer customer = customerRepo.findByEmailId(emailId)
				.orElseThrow(() -> new CustomerException("Customer Not Found"));
		Book book = bookRepo.findById(bookId).get();
		customer.getCartBook().add(book);
		customerRepo.save(customer);
		return customer;

	}

	public String placeOrder(CustomerDTO customerDTO, String token) {

		String email = jwtUtil.verify(token);
		Customer savedCustomer = customerRepo.findByEmailId(email)
				.orElseThrow(() -> new CustomerException("Customer Not Found"));
		double totalCost = savedCustomer.getCartBook().stream().map((order) -> order.getPrice()).reduce(0.0,
				Double::sum);

		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setTotalCost(totalCost);
		orderDTO.setCartBook(savedCustomer.getCartBook());

		savedCustomer.setPincode(customerDTO.getPincode());
		savedCustomer.setLocality(customerDTO.getLocality());
		savedCustomer.setAddress(customerDTO.getAddress());
		savedCustomer.setLandmark(customerDTO.getLandmark());
		savedCustomer.setCity(customerDTO.getCity());
		savedCustomer.setTypeOfAddress(customerDTO.getTypeOfAddress());
		customerRepo.save(savedCustomer);

//		emailSenderService.sendEmailWithTemplate(email, orderDTO);

		amqpTemplate.convertAndSend("user_exchange", "user_routingKey", email);
//		return orderDTO;
		return "Success";

	}

}
