package com.bl.service;

import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.comparator.Comparators;

import com.bl.Exception.CustomerException;
import com.bl.dto.BookDTO;
import com.bl.model.Book;
import com.bl.repository.BookRepository;
import com.bl.utils.ElasticSearchService;

@Service
public class BookService implements IBookService {

	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	ModelMapper mapper;
	
	@Autowired
	ElasticSearchService eSService;

	public Book convertDtoToEntity(BookDTO bookDto) {
		Book book = mapper.map(bookDto, Book.class);
		return book;
	}

	public BookDTO convertEntityToDto(Book book) {
		BookDTO bookDto = mapper.map(book, BookDTO.class);
		return bookDto;
	}

	public List<BookDTO> convertAllEntityToDto(List<Book> book) {
		return book.stream().map(this::convertEntityToDto).collect(Collectors.toList());
	}

	public BookDTO addBook(Book book) {
		bookRepo.findBybookName(book.getBookName()).ifPresent(existBook -> {
			throw new CustomerException("Book already added");
		});
		eSService.createBook(book);
		return convertEntityToDto(bookRepo.save(book));

	}

	public List<Book> listAllBook() {
		if (bookRepo.findAll().isEmpty()) {
			throw new CustomerException("No book details to show");
		}
		return eSService.searchData();
//		bookRepo.findAll()
//		return convertAllEntityToDto(eSService.searchData());
	}

	public BookDTO updateBook(Book book) {
		Book savedBook = bookRepo.findBybookName(book.getBookName()).get();
		savedBook.setBookName(book.getBookName());
		savedBook.setAuthor(book.getAuthor());
		savedBook.setPrice(book.getPrice());
		eSService.updateBook(savedBook);
		return convertEntityToDto(bookRepo.save(savedBook));
	}

	public List<BookDTO> getBookByName(String bookName, String token) throws IllegalArgumentException, UnsupportedEncodingException {
		bookRepo.findBybookName(bookName)
				.orElseThrow(() -> new CustomerException("Book with the given Name not found"));
//		return convertEntityToDto(bookRepo.findBybookName(bookName).get());
		return convertAllEntityToDto(eSService.searchAll(bookName, token));
		
	}

	public List<BookDTO> sortByPriceLToH(double price) {
		List<Book> books = bookRepo.findAll(Sort.by(Sort.Direction.ASC, "price"));
		if (books.isEmpty()) {
			throw new CustomerException("No book details to show");
		}
		return convertAllEntityToDto(books);
	}

	public List<BookDTO> sortByNewestArrival() {
		List<Book> books = bookRepo.findAll();

		if (books.isEmpty()) {
			throw new CustomerException("No book details to show");
		}
		List<Book> book = books.stream().collect(Collectors.collectingAndThen(Collectors.toList(), l -> {
			Collections.reverse(l);
			return l;
		}));

		return convertAllEntityToDto(book);

	}

	public List<BookDTO> sortByPriceHToL(double price) {

		List<Book> books = bookRepo.findAll(Sort.by(Sort.Direction.DESC, "price"));
		if (books.isEmpty()) {
			throw new CustomerException("No book details to show");
		}
//	 List<Book> book = books.stream().sorted(Comparator.comparingDouble(Book::getPrice).reversed()).
//			 collect(Collectors.toList());

		return convertAllEntityToDto(books);
	}

	public String deleteBookByName(String bookName) {

		Book book = bookRepo.findBybookName(bookName).get();
		bookRepo.findBybookName(bookName)
				.orElseThrow(() -> new CustomerException("Book with the given Name not found"));
		bookRepo.deleteById(book.getBookId());
		eSService.deleteBook(book.getBookId());
		return "Book with the given name is deleted Successfully";
	}
}
