package com.example.demo;

import org.junit.jupiter.api.Test; 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.boot.test.context.SpringBootTest; 
import org.springframework.boot.test.web.client.TestRestTemplate; 
import org.springframework.boot.test.web.server.LocalServerPort; 
import org.springframework.http.*; 
import static org.junit.jupiter.api.Assertions.*; 

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NegativeTest {
	
	@Autowired 
	private TestRestTemplate restTemplate; 
	
	@LocalServerPort 
	private int port; 
	private String baseUrl() { 
		return "http://localhost:" + port; 
	} 

	// TC_N01: menuId ไม่มีจริง 
	@Test 
	void TC_N01_testCreateOrderInvalidMenuId() { 
		String json = """ 
				{ 
					"items": [ 
						{ "menuId": 9999, "quantity": 1 } 
						] 
				} 
				"""; 
		
		HttpHeaders headers = new HttpHeaders(); 
		headers.setContentType(MediaType.APPLICATION_JSON); 
		
		HttpEntity<String> request = new HttpEntity<>(json, headers); 
		ResponseEntity<String> response = 
				restTemplate.postForEntity(baseUrl() + "/api/order", request, String.class); 
		
		assertTrue(response.getStatusCode().is4xxClientError()); 
	} 
	
	// TC_N02: JSON format ผิด 
	@Test 
	void TC_N02_testInvalidJsonFormat() { 
		String json = "{ invalid json }"; 
		
		HttpHeaders headers = new HttpHeaders(); 
		headers.setContentType(MediaType.APPLICATION_JSON); 
		HttpEntity<String> request = new HttpEntity<>(json, headers); 
		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl() + "/api/order", request, String.class); 
		
		assertTrue(response.getStatusCode().is4xxClientError()); 
	} 
	
	// TC_N03: ไม่ใส่ Content-Type 
	@Test 
	void TC_N03_testMissingContentType() { 
		String json = """ 
				{ 
					"items": [ 
						{ "menuId": 1, "quantity": 1 } 
						] 
				} 
				"""; 
		
		HttpHeaders headers = new HttpHeaders(); 
		
		// intentionally not setting content-type 
		HttpEntity<String> request = new HttpEntity<>(json, headers); 
		ResponseEntity<String> response = 
				restTemplate.postForEntity(baseUrl() + "/api/order", request, String.class); 
		
		assertTrue(response.getStatusCode().is4xxClientError()); 
	}
	
}


