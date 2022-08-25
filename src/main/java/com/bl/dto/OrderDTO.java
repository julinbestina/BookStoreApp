package com.bl.dto;

import java.util.LinkedList;
import java.util.List;

import com.bl.model.Book;

import lombok.Data;

@Data
public class OrderDTO {

	private List<Book> cartBook = new LinkedList<>();
	private double totalCost;
}
