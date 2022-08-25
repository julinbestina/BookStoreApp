package com.bl.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.bl.model.Book;
import com.bl.repository.BookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;



@Service
public class ElasticSearchService {

	String INDEX = "book";
	String TYPE = "booktype";
	
	@Autowired
	private RestHighLevelClient client;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	JWTUtil jwtUtil;


	public Book createBook (Book book) {
		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = objectMapper.convertValue(book, Map.class);
		IndexRequest indexRequest = new IndexRequest(INDEX , TYPE, String
				.valueOf(book.getBookId())).source(dataMap);
		
		try {
			client.index(indexRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return book;
	}
	
	
	public Book updateBook(Book book) {
		@SuppressWarnings("unchecked")
		Map<String, Object> dataMap = objectMapper.convertValue(book, Map.class);
		UpdateRequest updateRequest = new UpdateRequest(INDEX , TYPE, String.valueOf(book.getBookId()));
		
		updateRequest.doc(dataMap);
		
		try {
			client.update(updateRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return book;
	}
	
	public void deleteBook(int bookId) {
		DeleteRequest deleteRequest = new DeleteRequest(INDEX , TYPE, String.valueOf(bookId));		
		try {
			client.delete(deleteRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public List<Book> searchData() {
		SearchRequest searchRequest = new SearchRequest();
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;		
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getSearchResult(searchResponse);
	}


	private List<Book> getSearchResult(SearchResponse response) {
		SearchHit[] searchHit = response.getHits().getHits();
		List<Book> books = new ArrayList<>();
		
		if (searchHit.length > 0) {
			Arrays.stream(searchHit)
				.forEach(hit -> books.add(objectMapper.convertValue(hit.getSourceAsMap(),Book.class)));
		}
		return books;
	}
	
	public List<Book> searchAll(String query, String token) throws IllegalArgumentException, UnsupportedEncodingException{
		
		String email = jwtUtil.verify(token);
		SearchRequest searchRequest = new SearchRequest(INDEX).types(TYPE);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
											.must(QueryBuilders.queryStringQuery("*" +query+ "*")
													.analyzeWildcard(true).field("Author").field("bookName").field("price"))
											.filter(QueryBuilders.termsQuery("email", String.valueOf(email)));
				
		searchSourceBuilder.query(queryBuilder);
		searchRequest.source(searchSourceBuilder);
		SearchResponse searchResponse = null;		
		try {
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return getSearchResult(searchResponse);
	}
	
}
