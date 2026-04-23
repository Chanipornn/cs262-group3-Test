package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Unit Test: applyDiscount() - ระบบส่วนลดร้านอาหาร")
public class DiscountTest {

    // ข้อมูลจากระบบจริง
    // base = 50, discountPrice = 35
    // ประหยัด = 50 - 35 = 15 บาท

    double basePrice = 50.0;
    double discountPrice = 35.0;

    // ฟังก์ชัน applyDiscount จำลองจาก discount.js
    double applyDiscount(double base, double discount) {
        if (base < 0 || discount < 0) {
            throw new IllegalArgumentException("ราคาต้องไม่ติดลบ");
        }
        if (discount > base) {
            throw new IllegalArgumentException("ราคาลดต้องไม่มากกว่าราคาเต็ม");
        }
        return base - discount;
    }

    // ฟังก์ชัน calcPerDish จำลองจาก discount.js
    double calcPerDish(double discountBase, double modifierPrice, 
                       int addonId, int addonQty, double addonPrice) {
        double total = discountBase;
        total += modifierPrice;
        total += addonQty * addonPrice;
        return total;
    }

    // ============================================
    // TC01 - ปกติ: ราคา 50 ลดเหลือ 35 ประหยัด 15
    // ============================================
    @Test
    @DisplayName("TC01 - ราคาเต็ม 50 ลดเหลือ 35 ประหยัด 15 บาท")
    void testApplyDiscount_Normal() {
        double saved = applyDiscount(basePrice, discountPrice);
        assertEquals(15.0, saved, 0.001,
            "ราคาเต็ม 50 - ราคาลด 35 ต้องประหยัด 15 บาท");
    }

    // ============================================
    // TC02 - ไม่มีส่วนลด (base = discount)
    // ============================================
    @Test
    @DisplayName("TC02 - ไม่มีส่วนลด ประหยัด 0 บาท")
    void testApplyDiscount_NoDiscount() {
        double saved = applyDiscount(50.0, 50.0);
        assertEquals(0.0, saved, 0.001,
            "ราคาเท่ากัน ต้องประหยัด 0 บาท");
    }

    // ============================================
    // TC03 - คำนวณราคาต่อจาน: ปกติ ไม่มี addon
    // discountPrice(35) + modifier(0) + addon(0) = 35
    // ============================================
    @Test
    @DisplayName("TC03 - เลือกไซส์ปกติ ไม่มี addon ราคา 35 บาท")
    void testCalcPerDish_Normal_NoAddon() {
        double result = calcPerDish(35.0, 0.0, 3, 0, 10.0);
        assertEquals(35.0, result, 0.001,
            "discountPrice 35 + modifier 0 + addon 0 = 35");
    }

    // ============================================
    // TC04 - เลือกพิเศษ (+10) ไม่มี addon
    // 35 + 10 + 0 = 45
    // ============================================
    @Test
    @DisplayName("TC04 - เลือกไซส์พิเศษ +10 ไม่มี addon ราคา 45 บาท")
    void testCalcPerDish_Special_NoAddon() {
        double result = calcPerDish(35.0, 10.0, 3, 0, 10.0);
        assertEquals(45.0, result, 0.001,
            "discountPrice 35 + modifier 10 = 45");
    }

    // ============================================
    // TC05 - เลือกปกติ + ไข่เจียว 1 ชิ้น
    // 35 + 0 + (1 × 10) = 45
    // ============================================
    @Test
    @DisplayName("TC05 - ปกติ + ไข่เจียว 1 ชิ้น ราคา 45 บาท")
    void testCalcPerDish_Normal_OneAddon() {
        double result = calcPerDish(35.0, 0.0, 3, 1, 10.0);
        assertEquals(45.0, result, 0.001,
            "35 + addon 1×10 = 45");
    }

    // ============================================
    // TC06 - Boundary: addon สูงสุด 5 ชิ้น
    // 35 + 0 + (5 × 10) = 85
    // ============================================
    @Test
    @DisplayName("TC06 - addon สูงสุด 5 ชิ้น ราคา 85 บาท")
    void testCalcPerDish_MaxAddon() {
        double result = calcPerDish(35.0, 0.0, 3, 5, 10.0);
        assertEquals(85.0, result, 0.001,
            "35 + addon 5×10 = 85");
    }

    // ============================================
    // TC07 - ราคาติดลบ → ต้อง throw Exception
    // ============================================
    @Test
    @DisplayName("TC07 - ราคาติดลบ ต้อง throw IllegalArgumentException")
    void testApplyDiscount_NegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            applyDiscount(-50.0, 35.0);
        }, "ราคาติดลบต้อง throw exception");
    }

    // ============================================
    // TC08 - ราคาลดมากกว่าราคาเต็ม → throw Exception
    // ============================================
    @Test
    @DisplayName("TC08 - ราคาลดมากกว่าราคาเต็ม ต้อง throw Exception")
    void testApplyDiscount_DiscountOverBase() {
        assertThrows(IllegalArgumentException.class, () -> {
            applyDiscount(30.0, 50.0);
        }, "ราคาลดเกินต้อง throw exception");
    }
}
