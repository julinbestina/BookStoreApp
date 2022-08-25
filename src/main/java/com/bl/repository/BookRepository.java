package com.bl.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.bl.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{

	public Optional<Book> findBybookName(String bookName);
	
	
}
