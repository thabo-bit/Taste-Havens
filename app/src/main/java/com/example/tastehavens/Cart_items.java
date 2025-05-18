package com.example.tastehavens;

import java.io.Serializable;

public class Cart_items implements Serializable {
    private String mealName;
    private String price;
    private int quantity;
    private String imageUrl;
    private String documentId; // Add this field to track Firestore document ID

    public Cart_items(String mealName, String price, int quantity, String imageUrl) {
        this.mealName = mealName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // Getters
    public String getMealName() { return mealName; }
    public String getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getImageUrl() { return imageUrl; }
    public String getDocumentId() { return documentId; }

    // Setters
    public void setMealName(String mealName) { this.mealName = mealName; }
    public void setPrice(String price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
}