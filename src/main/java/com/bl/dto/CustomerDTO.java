package com.bl.dto;

import lombok.Data;

@Data
public class CustomerDTO {

	private int pincode;
	private String locality;
	private String address;
	private String city;
	private String landmark;
	private String typeOfAddress;
	private String country;
}
