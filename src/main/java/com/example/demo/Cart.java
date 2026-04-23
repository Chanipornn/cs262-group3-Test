package com.example.demo;

import java.util.*;

class CartItem {
    String name;
    int price;
    int quantity;

    CartItem(String name, int price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}

public class Cart {
    private Map<String, CartItem> items = new HashMap<>();

    public void addToCart(String name, int price) {
        if (items.containsKey(name)) {
            items.get(name).quantity++;
        } else {
            items.put(name, new CartItem(name, price, 1));
        }
    }

    public void removeFromCart(String name) {
        items.remove(name);
    }

    public void updateQuantity(String name, int quantity) {
        if (items.containsKey(name)) {
            if (quantity <= 0) {
                items.remove(name);
            } else {
                items.get(name).quantity = quantity;
            }
        }
    }

    public Map<String, CartItem> getItems() {
        return items;
    }
}