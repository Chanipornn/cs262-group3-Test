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
    void TC_P01_testLoadMenuPerformance() {

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

        System.out.println("Average Response Time: " + avgTime + " ms");

        assertTrue(avgTime < 2000); // < 2 วินาที
    }
    
    // TC_P02: Baseline Test (โหลดเมนู)
    @Test
    void TC_P02_testConcurrentUsers() throws InterruptedException {

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

        System.out.println("Concurrent test ผ่าน (20 users)");
    }
    
    
    // TC_P03: โหลดหน้าไม่เกิน 3 วิ
    @Test
    void TC_P03_testPageLoadUnder3Seconds() {

        int requestCount = 10;
        long totalTime = 0;

        for (int i = 0; i < requestCount; i++) {

            long start = System.currentTimeMillis();

            ResponseEntity<String> response =
                    restTemplate.getForEntity(baseUrl() + "/api/menu", String.class);

            long end = System.currentTimeMillis();
            long responseTime = end - start;

            totalTime += responseTime;

            assertEquals(HttpStatus.OK, response.getStatusCode());

            // เช็คแต่ละ request
            assertTrue(responseTime < 3000,
                    "ช้าเกิน: " + responseTime + " ms");
        }

        long avgTime = totalTime / requestCount;

        System.out.println("Avg Load (<3s): " + avgTime + " ms");

        // เช็คค่าเฉลี่ย
        assertTrue(avgTime < 3000);
    }

}
