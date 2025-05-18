package com.example.tastehavens;

import java.io.Serializable;
import java.util.List;

public class Checkout_WholeOrder implements Serializable {
    private String userId;
    private String customerName;
    private String orderType;
    private String notes;
    private String orderId;
    private List<Cart_items> cartItems;
    private String timestamp;

    public Checkout_WholeOrder(String userId, String customerName, String orderType,
                               String notes, String orderId, List<Cart_items> cartItems,
                               String timestamp) {
        this.userId = userId;
        this.customerName = customerName;
        this.orderType = orderType;
        this.notes = notes;
        this.orderId = orderId;
        this.cartItems = cartItems;
        this.timestamp = timestamp;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getCustomerName() { return customerName; }
    public String getOrderType() { return orderType; }
    public String getNotes() { return notes; }
    public String getOrderId() { return orderId; }
    public List<Cart_items> getCartItems() { return cartItems; }
    public String getTimestamp() { return timestamp; }
}