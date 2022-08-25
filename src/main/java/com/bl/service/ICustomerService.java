package com.bl.service;

import com.bl.dto.CustomerDTO;
import com.bl.dto.LoginDTO;
import com.bl.dto.OrderDTO;
import com.bl.dto.SignUpDTO;
import com.bl.model.Customer;

public interface ICustomerService {
	public Customer convertCusDtoToEntity(SignUpDTO loginCustomer);
	public Customer signUp(Customer customer);
	public String Login(LoginDTO loginCustomer);
	public Customer addToCart(int bookId, String token);
	public String placeOrder(CustomerDTO customerDTO, String token);

}
