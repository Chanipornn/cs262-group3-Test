package com.example.demo;

import com.example.demo.model.Menu;
import com.example.demo.model.OrderType;
import com.example.demo.repo.MenuRepository;
import com.example.demo.repo.OrderRepository;
import com.example.demo.repo.OrderTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") 
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTypeRepository orderTypeRepository;

    // 🔥 เตรียมข้อมูลก่อน test
    @BeforeEach
    void setup() {
        orderRepository.deleteAll();
        menuRepository.deleteAll();
        orderTypeRepository.deleteAll();

        // insert menu
        Menu menu = new Menu();
        menu.setId(1);
        menu.setName("Pizza");
        menu.setPrice(100.0);
        menuRepository.save(menu);

        // insert order type
        OrderType type = new OrderType();
        type.setId(1);
        type.setType("Dine-in");
        orderTypeRepository.save(type);
    }

    // TC_I01
    @Test
    void testCreateOrder() throws Exception {

        String requestJson = """
        {
          "totalAmount": 200,
          "orderTypeId": 1,
          "items": [
            {
              "menuId": 1,
              "quantity": 2,
              "additionalPrice": 0,
              "noteText": ""
            }
          ]
        }
        """;

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }

    // TC_I02
    @Test
    void testOrderSavedToDatabase() throws Exception {

        String requestJson = """
        {
          "totalAmount": 100,
          "orderTypeId": 1,
          "items": [
            {
              "menuId": 1,
              "quantity": 1,
              "additionalPrice": 0,
              "noteText": ""
            }
          ]
        }
        """;

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());

        assertFalse(orderRepository.findAll().isEmpty());
    }

    // TC_I03
    @Test
    void testResponseContainsTotal() throws Exception {

        String requestJson = """
        {
          "totalAmount": 200,
          "orderTypeId": 1,
          "items": [
            {
              "menuId": 1,
              "quantity": 2,
              "additionalPrice": 0,
              "noteText": ""
            }
          ]
        }
        """;

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(200));
    }
}