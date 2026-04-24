package com.example.demo;

import com.example.demo.model.Menu;
import com.example.demo.repo.MenuRepository;
import com.example.demo.service.MenuService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/* Unit Test (ทดสอบ logic) 
   เทสว่า: filter, search ถูกมั้ย
 */
public class MenuServiceTest {
	private MenuRepository repo;
    private MenuService service;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(MenuRepository.class);
        service = new MenuService(repo);

        Menu m1 = new Menu();
        m1.setName("Pizza");
        m1.setCategoryId(1L);

        Menu m2 = new Menu();
        m2.setName("Coke");
        m2.setCategoryId(2L);

        Mockito.when(repo.findAll()).thenReturn(Arrays.asList(m1, m2));
    }

    // ค้นหาเจอ
    @Test
    void testSearch01_found() {
        List<Menu> result = service.searchMenu("Pizza", null);
        assertEquals(1, result.size());
        assertEquals("Pizza", result.get(0).getName());
    }

    // ค้นหาไม่เจอ
    @Test
    void testSearch02_notFound() {
        List<Menu> result = service.searchMenu("Burger", null);
        assertTrue(result.isEmpty());
    }

    // input ว่าง
    @Test
    void testSearch03_emptyQuery() {
        List<Menu> result = service.searchMenu("", null);
        assertEquals(2, result.size());
    }

    // filter category
    @Test
    void testSearch04_filterCategory() {
        List<Menu> result = service.searchMenu(null, 2L);
        assertEquals(1, result.size());
        assertEquals("Coke", result.get(0).getName());
    }
    
    // case insensitive
    @Test
    void testSearch05_caseInsensitive() {
        List<Menu> result = service.searchMenu("pizza", null);

        assertEquals(1, result.size());
    }

    // invalid category
    @Test
    void testSearch06_invalidCategory() {
        List<Menu> result = service.searchMenu(null, 999L);

        assertTrue(result.isEmpty());
    }

}
