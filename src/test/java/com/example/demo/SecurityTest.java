package com.example.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Security Testing - ระบบร้านอาหาร")
public class SecurityTest {

    // ===== จำลอง Input Validation =====
    String validateMenuId(int menuId) {
        if (menuId <= 0) {
            return "ERROR_400: Invalid menuId";
        }
        return "OK";
    }

    // ===== จำลอง SQL Injection Prevention =====
    String sanitizeSQLInput(String input) {
        if (input == null) return "";
        // ตรวจจับ SQL keywords อันตราย
        String[] dangerous = {"'", "--", ";", "DROP", "SELECT", 
                              "INSERT", "DELETE", "UPDATE", "OR 1=1"};
        for (String d : dangerous) {
            if (input.toUpperCase().contains(d.toUpperCase())) {
                return "BLOCKED";
            }
        }
        return input;
    }

    // ===== จำลอง XSS Prevention =====
    String sanitizeXSS(String input) {
        if (input == null) return "";
        return input
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
            .replace("/", "&#x2F;");
    }

    // ============================================
    // TC_S01 - Input Validation
    // ส่ง menuId = -1 → ต้องได้ ERROR_400
    // ============================================
    @Test
    @DisplayName("TC_S01 - Input Validation: menuId ติดลบ ต้อง reject")
    void testInputValidation_NegativeMenuId() {
        String result = validateMenuId(-1);
        assertEquals("ERROR_400: Invalid menuId", result,
            "menuId = -1 ต้องถูก reject ด้วย status 400");
    }

    // TC_S01 เพิ่มเติม: menuId = 0
    @Test
    @DisplayName("TC_S01b - Input Validation: menuId = 0 ต้อง reject")
    void testInputValidation_ZeroMenuId() {
        String result = validateMenuId(0);
        assertEquals("ERROR_400: Invalid menuId", result,
            "menuId = 0 ต้องถูก reject");
    }

    // TC_S01 เพิ่มเติม: menuId ถูกต้อง
    @Test
    @DisplayName("TC_S01c - Input Validation: menuId ถูกต้อง ต้องผ่าน")
    void testInputValidation_ValidMenuId() {
        String result = validateMenuId(1);
        assertEquals("OK", result,
            "menuId = 1 ต้องผ่าน");
    }

    // ============================================
    // TC_S02 - SQL Injection Prevention
    // ส่ง malicious input → ต้องถูก BLOCK
    // ============================================
    @Test
    @DisplayName("TC_S02 - SQL Injection: DROP TABLE ต้องถูก block")
    void testSQLInjection_DropTable() {
        String malicious = "'; DROP TABLE orders; --";
        String result = sanitizeSQLInput(malicious);
        assertEquals("BLOCKED", result,
            "SQL injection ต้องถูก block");
    }

    @Test
    @DisplayName("TC_S02b - SQL Injection: OR 1=1 ต้องถูก block")
    void testSQLInjection_Or1Equals1() {
        String malicious = "' OR 1=1 --";
        String result = sanitizeSQLInput(malicious);
        assertEquals("BLOCKED", result,
            "OR 1=1 ต้องถูก block");
    }

    @Test
    @DisplayName("TC_S02c - SQL Injection: input ปกติต้องผ่าน")
    void testSQLInjection_NormalInput() {
        String normal = "สุกี้น้ำหมู";
        String result = sanitizeSQLInput(normal);
        assertEquals("สุกี้น้ำหมู", result,
            "input ปกติต้องไม่ถูก block");
    }

    // ============================================
    // TC_S03 - XSS Prevention
    // ส่ง <script> → ต้องถูก sanitize
    // ============================================
    @Test
    @DisplayName("TC_S03 - XSS: <script> ต้องถูก sanitize")
    void testXSS_ScriptTag() {
        String malicious = "<script>alert('XSS')</script>";
        String result = sanitizeXSS(malicious);
        assertFalse(result.contains("<script>"),
            "script tag ต้องถูก sanitize");
        assertTrue(result.contains("&lt;script&gt;"),
            "ต้องแปลง < > เป็น HTML entities");
    }

    @Test
    @DisplayName("TC_S03b - XSS: input ปกติต้องไม่เปลี่ยน < >")
    void testXSS_NormalText() {
        String normal = "ราคา 50 บาท";
        String result = sanitizeXSS(normal);
        assertEquals("ราคา 50 บาท", result,
            "ข้อความปกติต้องไม่เปลี่ยนแปลง");
    }

    @Test
    @DisplayName("TC_S03c - XSS: img tag ต้องถูก sanitize")
    void testXSS_ImgTag() {
        String malicious = "<img src=x onerror=alert(1)>";
        String result = sanitizeXSS(malicious);
        assertFalse(result.contains("<img"),
            "img tag ต้องถูก sanitize");
    }
}