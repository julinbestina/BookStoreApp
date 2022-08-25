package com.bl.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import com.bl.dto.BookDTO;
import com.bl.model.Book;

public interface IBookService {
	
	public BookDTO addBook(Book book);

	public Book convertDtoToEntity(BookDTO bookDto);

	public BookDTO convertEntityToDto(Book book);

	public List<Book> listAllBook();

	public BookDTO updateBook(Book book);

	public List<BookDTO> getBookByName(String bookName, String token) throws IllegalArgumentException, UnsupportedEncodingException;
	public List<BookDTO> sortByPriceLToH(double price);

	public List<BookDTO> sortByPriceHToL(double price);

	public List<BookDTO> sortByNewestArrival();

	public String deleteBookByName(String bookName);

}
