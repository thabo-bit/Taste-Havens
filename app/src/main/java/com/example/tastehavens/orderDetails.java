package com.example.tastehavens;

import java.util.List;

public class orderDetails {
    private String orderId;
    private String tableNumber;
    private String orderTime;
    private String customerName;
    private List<Cart_items> cartItems;
    private String notes;
    private String userId;

    // Constructor
    public orderDetails(String orderId, String tableNumber, String orderTime,
                        String customerName, List<Cart_items> cartItems,
                        String notes, String userId) {
        this.orderId = orderId;
        this.tableNumber = tableNumber;
        this.orderTime = orderTime;
        this.customerName = customerName;
        this.cartItems = cartItems;
        this.notes = notes;
        this.userId = userId;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getTableNumber() { return tableNumber; }
    public String getOrderTime() { return orderTime; }
    public String getCustomerName() { return customerName; }
    public List<Cart_items> getCartItems() { return cartItems; }
    public String getNotes() { return notes; }
    public String getUserId() { return userId; }
}