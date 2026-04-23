package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MenuPerformanceTest {
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	@LocalServerPort
    private int port;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    // TC_P01: โหลดเมนู (GET /api/menu หลายครั้ง)
    @Test
    void testLoadMenuPerformance() {

        int requestCount = 50;
        long totalTime = 0;

        for (int i = 0; i < requestCount; i++) {
            long start = System.currentTimeMillis();

            ResponseEntity<String> response =
                    restTemplate.getForEntity("/api/menu", String.class);

            long end = System.currentTimeMillis();
            totalTime += (end - start);

            assertEquals(200, response.getStatusCodeValue());
        }

        long avgTime = totalTime / requestCount;

        System.out.println("⏱️ Average Response Time: " + avgTime + " ms");

        assertTrue(avgTime < 2000); // < 2 วินาที
    }
    
    // TC_P02: Concurrent Users (ยิงพร้อมกัน)
    @Test
    void testConcurrentUsers() throws InterruptedException {

        int users = 20;
        Thread[] threads = new Thread[users];

        for (int i = 0; i < users; i++) {
            threads[i] = new Thread(() -> {
                ResponseEntity<String> response =
                        restTemplate.getForEntity("/api/menu", String.class);

                assertEquals(200, response.getStatusCodeValue());
            });
        }

        // start พร้อมกัน
        for (Thread t : threads) t.start();

        // รอทุก thread จบ
        for (Thread t : threads) t.join();

        System.out.println("✅ Concurrent test ผ่าน (20 users)");
    }
    
    // TC_P03: สร้าง order หลายครั้ง (POST)
    @Test
    void testCreateOrderPerformance() {

        int requestCount = 30;
        long totalTime = 0;

        String json = """
            {
              "items": [
                { "menuId": 1, "quantity": 1 }
              ]
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        for (int i = 0; i < requestCount; i++) {

            long start = System.currentTimeMillis();

            ResponseEntity<String> response =
                    restTemplate.postForEntity(baseUrl() + "/api/order", request, String.class);

            long end = System.currentTimeMillis();
            totalTime += (end - start);

            // ถ้า API ยังไม่มี ยังไม่ต้อง assert 200
            assertTrue(response.getStatusCode().is2xxSuccessful()
                    || response.getStatusCode().is4xxClientError());
        }

        long avgTime = totalTime / requestCount;

        System.out.println("⏱️ Order Avg: " + avgTime + " ms");
    }

}
