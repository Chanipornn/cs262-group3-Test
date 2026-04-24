package com.example.demo;

import com.example.demo.controller.MenuController;
import com.example.demo.model.Menu;
import com.example.demo.service.MenuService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*	Controller Test (ทดสอบ API)
	เทสว่า:	endpoint ทำงานมั้ย
	 		parameter ถูกมั้ย
			return JSON ถูกมั้ย
*/
@WebMvcTest(MenuController.class)
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService service;

    // helper
    private Menu createMenu(String name, Long categoryId) {
        Menu m = new Menu();
        m.setName(name);
        m.setCategoryId(categoryId);
        return m;
    }

    // ค้นหาเจอ
    @Test
    void A01_testSearchfound() throws Exception {

        Menu m = createMenu("Pizza", 1L);

        Mockito.when(service.searchMenu(eq("Pizza"), isNull()))
                .thenReturn(List.of(m));

        mockMvc.perform(get("/api/menu/search?q=Pizza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Pizza"));
    }

    // ค้นหาไม่เจอ
    @Test
    void A02_testSearchnotFound() throws Exception {

        Mockito.when(service.searchMenu(eq("Burger"), isNull()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/menu/search?q=Burger"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    // input ว่าง → ต้องได้ทั้งหมด
    @Test
    void A03_testSearchemptyQuery() throws Exception {

        Menu m1 = createMenu("Pizza", 1L);
        Menu m2 = createMenu("Coke", 2L);

        Mockito.when(service.searchMenu(isNull(), isNull()))
                .thenReturn(List.of(m1, m2));

        mockMvc.perform(get("/api/menu/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    // filter category
    @Test
    void A04_testSearchfilterCategory() throws Exception {

        Menu m = createMenu("Coke", 2L);

        Mockito.when(service.searchMenu(isNull(), eq(2L)))
                .thenReturn(List.of(m));

        mockMvc.perform(get("/api/menu/search?categoryId=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Coke"));
    }

    // search + filter
    @Test
    void A05_testSearchsearchAndFilter() throws Exception {

        Menu m = createMenu("Pizza", 1L);

        Mockito.when(service.searchMenu(eq("Pizza"), eq(1L)))
                .thenReturn(List.of(m));

        mockMvc.perform(get("/api/menu/search?q=Pizza&categoryId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Pizza"));
    }

    // case insensitive
    @Test
    void A06_testSearchcaseInsensitive() throws Exception {

        Menu m = createMenu("Pizza", 1L);

        Mockito.when(service.searchMenu(eq("pizza"), isNull()))
                .thenReturn(List.of(m));

        mockMvc.perform(get("/api/menu/search?q=pizza"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza"));
    }

    // category ไม่มี
    @Test
    void A07_testSearchinvalidCategory() throws Exception {

        Mockito.when(service.searchMenu(isNull(), eq(999L)))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/menu/search?categoryId=999"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}