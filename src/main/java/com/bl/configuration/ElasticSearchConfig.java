package com.bl.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.elasticsearch.client.RestHighLevelClient;


@Configuration
public class ElasticSearchConfig {
	
	
	    @Bean
	    RestHighLevelClient elasticsearchClient() {

	    	RestHighLevelClient client = new RestHighLevelClient(RestClient
	    			.builder(new HttpHost("localhost", 9200, "http")));
	     
	    return client;
	    }
}






