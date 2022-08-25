package com.bl.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bl.dto.BookDTO;
import com.bl.model.Book;
import com.bl.service.BookService;
import com.bl.service.IBookService;
import com.bl.utils.JWTUtil;

@RestController
@RequestMapping("bookstore")
public class BookController {

	@Autowired
	IBookService bookService;

	@Autowired
	JWTUtil jwtUtil;

	@PostMapping
	public ResponseEntity<String> addBook(@RequestHeader String token, @RequestBody BookDTO bookDto) {
		jwtUtil.verify(token);
		Book book = bookService.convertDtoToEntity(bookDto);
		bookService.addBook(book);
		return ResponseEntity.status(HttpStatus.OK).body("Book Details Added Successfully");
	}

	@PutMapping
	public ResponseEntity<String> updateBook(@RequestHeader String token, @RequestBody BookDTO bookDto) {
		jwtUtil.verify(token);
		Book book = bookService.convertDtoToEntity(bookDto);
		bookService.updateBook(book);
		return ResponseEntity.status(HttpStatus.OK).body("Book Details updated Successfully");
	}

	@GetMapping
	public ResponseEntity<List<Book>> listAllBooks(@RequestHeader String token) {
		jwtUtil.verify(token);
		return ResponseEntity.status(HttpStatus.OK).body(bookService.listAllBook());
	}

	@GetMapping("/{bookName}")
	public ResponseEntity<List<BookDTO>> getBookByName(@RequestHeader String token, @PathVariable String bookName) throws IllegalArgumentException, UnsupportedEncodingException {
		jwtUtil.verify(token);
		return ResponseEntity.status(HttpStatus.OK).body(bookService.getBookByName(bookName, token));
	}

	@GetMapping("/lowtohigh")
	public ResponseEntity<List<BookDTO>> sortByPriceLToH(@RequestHeader String token) {
		jwtUtil.verify(token);
		Book book = new Book();
		return ResponseEntity.status(HttpStatus.OK).body(bookService.sortByPriceLToH(book.getPrice()));
	}

	@GetMapping("/hightolow")
	public ResponseEntity<List<BookDTO>> sortByPriceHToL(@RequestHeader String token) {
		jwtUtil.verify(token);
		Book book = new Book();
		return ResponseEntity.status(HttpStatus.OK).body(bookService.sortByPriceHToL(book.getPrice()));
	}

	@GetMapping("/newarrivals")
	public ResponseEntity<List<BookDTO>> newArrivals(@RequestHeader String token) {
		jwtUtil.verify(token);
		return ResponseEntity.status(HttpStatus.OK).body(bookService.sortByNewestArrival());
	}

	@DeleteMapping("/{bookName}")
	public ResponseEntity<String> deleteBook(@RequestHeader String token, @PathVariable String bookName) {
		jwtUtil.verify(token);
		return ResponseEntity.status(HttpStatus.OK).body(bookService.deleteBookByName(bookName));

	}

}