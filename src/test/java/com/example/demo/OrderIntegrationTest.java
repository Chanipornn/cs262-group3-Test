package com.example.demo;

import com.example.demo.controller.OrderController;
import com.example.demo.model.Orders;
import com.example.demo.repo.MenuRepository;
import com.example.demo.repo.OrderRepository;
import com.example.demo.repo.OrderTypeRepository;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private MenuRepository menuRepository;

    @MockBean
    private OrderTypeRepository orderTypeRepository;

    // ==============================
    // TC_I01: create order
    // ==============================
    @Test
    void testCreateOrder() throws Exception {

        Orders mockOrder = new Orders();
        mockOrder.setTotalAmount(200.0);

        when(orderRepository.save(any())).thenReturn(mockOrder);

        String requestJson = """
        {
          "totalAmount": 200
        }
        """;

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    // ==============================
    // TC_I02: verify save called
    // ==============================
    @Test
    void testOrderSavedToDatabase() throws Exception {

        Orders mockOrder = new Orders();
        mockOrder.setTotalAmount(100.0);

        when(orderRepository.save(any())).thenReturn(mockOrder);

        String requestJson = """
        {
          "totalAmount": 100
        }
        """;

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());

        verify(orderRepository).save(any()); // 👈 สำคัญ
    }

    // ==============================
    // TC_I03: response contains total
    // ==============================
    @Test
    void testResponseContainsTotal() throws Exception {

        Orders mockOrder = new Orders();
        mockOrder.setTotalAmount(200.0);

        when(orderRepository.save(any())).thenReturn(mockOrder);

        String requestJson = """
        {
          "totalAmount": 200
        }
        """;

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(200));
    }
}