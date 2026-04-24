package com.example.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

    @Test
    void test01_AddToCart() {
        Cart cart = new Cart();
        cart.addToCart("Pizza", 100);

        assertEquals(1, cart.getItems().size());
    }

    @Test
    void test02_RemoveFromCart() {
        Cart cart = new Cart();
        cart.addToCart("Pizza", 100);
        cart.removeFromCart("Pizza");

        assertEquals(0, cart.getItems().size());
    }

    @Test
    void test03_UpdateQuantity() {
        Cart cart = new Cart();
        cart.addToCart("Pizza", 100);
        cart.updateQuantity("Pizza", 3);

        assertEquals(3, cart.getItems().get("Pizza").quantity);
    }
}