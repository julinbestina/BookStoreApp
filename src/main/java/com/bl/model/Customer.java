package com.bl.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.OneToMany;
import javax.validation.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int customerId;
	@NotNull(message = "Name should not be null")
	private String customerName;
	@Email(message = "Invalid emailId")
	private String emailId;
	@NotNull
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$")
	private String password;
	@NotNull
	@Size(max = 10)
	private long phoneNumber;
	@NotNull
	private int pincode;
	@NotNull
	private String locality;
	@NotNull
	private String address;
	@NotNull
	private String city;
	@NotNull
	private String landmark;
	@NotNull
	private String typeOfAddress;
	@NotNull
	private String country;

	@OneToMany
	private List<Book> cartBook = new LinkedList<>();

}
