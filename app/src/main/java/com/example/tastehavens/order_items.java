package com.example.tastehavens;

import java.util.List;

public class order_items {
    private String customerName;
    private List<String> items;
    private String tableNumber;
    private long timestamp;

    public order_items() {} // Needed for Firebase

    public order_items(String customerName, List<String> items, String tableNumber, long timestamp) {
        this.customerName = customerName;
        this.items = items;
        this.tableNumber = tableNumber;
        this.timestamp = timestamp;
    }

    public String getCustomerName() { return customerName; }
    public List<String> getItems() { return items; }
    public String getTableNumber() { return tableNumber; }
    public long getTimestamp() { return timestamp; }
}
