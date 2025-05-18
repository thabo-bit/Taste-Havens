package com.example.tastehavens;

import java.util.Date;

public class Checkout_Items {
    private String mealName; // Added mealName
    private String customerName;
    private String orderType;
    private String specialNotes;
    private String orderId;
    private long quantity;
    private String timestamp;

    public Checkout_Items() {
        // Needed for Firestore
    }

    public Checkout_Items(String mealName, String customerName, String orderType, String specialNotes, long quantity, String orderId) {
        this.mealName = mealName;
        this.customerName = customerName;
        this.orderType = orderType;
        this.specialNotes = specialNotes;
        this.orderId = orderId;
        this.quantity = quantity;
        this.timestamp = new Date().toString(); // Added timestamp
    }

    // getters
    public String getMealName() { return mealName; }
    public String getCustomerName() {
        return customerName; }
    public String getOrderType() {
        return orderType; }
    public String getSpecialNotes() {
        return specialNotes; }
    public String getOrderId() {
        return orderId; }
    public long getQuantity() {
        return quantity; }
    public String getTimestamp() {
        return timestamp; }


}