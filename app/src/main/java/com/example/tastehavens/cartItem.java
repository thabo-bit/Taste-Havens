package com.example.tastehavens;

public class cartItem {
    private String mealName;
    private String price;
    private int quantity;
    private String imageUrl;

    public cartItem() {
        // Needed for Firestore
    }

    public cartItem(String mealName, String price, int quantity, String imageUrl) {
        this.mealName = mealName;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    // getters
    public String getMealName() { return mealName; }
    public String getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getImageUrl() { return imageUrl; }
}
