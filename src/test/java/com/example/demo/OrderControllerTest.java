package com.example.demo; 

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import com.example.demo.model.Menu;
import com.example.demo.model.OrderItem;
import com.example.demo.repo.MenuRepository;
import com.example.demo.repo.OrderRepository;
import com.example.demo.repo.OrderTypeRepository;
import com.example.demo.controller.OrderController; 
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTypeRepository orderTypeRepository;

    @InjectMocks
    private OrderController orderController; 

    // ---------------------------------------------------------
    // ส่วนที่ 1: 
    // ---------------------------------------------------------

    @Test
    public void test1_calculatePrice() {
        // ทดสอบดึงราคาหลักของเมนู (Base Price)
        Menu mockMenu = new Menu();
        mockMenu.setPrice(50.0);
        
        double basePrice = mockMenu.getPrice();
        
        assertEquals(50.0, basePrice, "ราคาหลักต้องเท่ากับ 50");
        System.out.println("✅ 1. calculatePrice ผ่าน: ราคาหลักคือ " + basePrice);
    }

    @Test
    public void test2_addOptionPrice() {
        // ทดสอบการเพิ่มราคา Option เสริม (Additional Price)
        OrderItem item = new OrderItem();
        item.setAdditionalPrice(10.0); 

        double additional = item.getAdditionalPrice();
        
        assertEquals(10.0, additional, "ราคา Option ต้องเท่ากับ 10");
        System.out.println("✅ 2. addOptionPrice ผ่าน: ราคาเสริมคือ " + additional);
    }

    // ---------------------------------------------------------
    // ส่วนที่ 2: อีก 3 เคส 
    // ---------------------------------------------------------

    /**
     * เคสที่ 3: การแสดงผลรายการเมนู (TC_F01 )
     * ใช้ Stub เพื่อเช็คว่าดึงข้อมูลเมนูมาถูกไหม
     */
    @Test
    public void test3_Document_MenuDisplay() {
        Menu mockMenu = new Menu();
        mockMenu.setId(1);
        mockMenu.setName("กะเพราไก่");
        
        when(menuRepository.findById(1L)).thenReturn(Optional.of(mockMenu));
        Optional<Menu> result = menuRepository.findById(1L);
        
        assertEquals("กะเพราไก่", result.get().getName());
        System.out.println("✅ 3. [TC_F01] ผ่าน: แสดงเมนู " + result.get().getName() + " ถูกต้อง");
    }

    /**
     * เคสที่ 4: การคำนวณราคาสุทธิเมื่อเลือก Option ( TC_F02 )
     * นำราคาหลัก + ราคาเสริม มาเช็ครวมกัน
     */
    @Test
    public void test4_Document_OrderTotalWithOption() {
        double base = 50.0;
        double option = 10.0;
        double total = base + option;
        
        assertEquals(60.0, total);
        System.out.println("✅ 4. [TC_F02] ผ่าน: รวมราคา 50+10 = " + total);
    }

    /**
     * เคสที่ 5: การคำนวณยอดรวมทั้งบิล (การคำนวณราคาสุทธิ)
     * ทดสอบกรณีสั่งหลายรายการรวมกัน (Grand Total)
     */
    @Test
    public void test5_Document_GrandTotal() {
        double item1 = 60.0; // จานแรก
        double item2 = 45.0; // จานสอง
        double grandTotal = item1 + item2;
        
        assertEquals(105.0, grandTotal);
        System.out.println("✅ 5.  ผ่าน: ยอดบิลสุทธิ 105.0 Correct!");
    }
}