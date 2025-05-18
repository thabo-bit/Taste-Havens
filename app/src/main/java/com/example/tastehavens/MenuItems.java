package com.example.tastehavens;



public class MenuItems {
    private String MenuName;
    private String Price;
    private String imageUrl;
    private  String Discription;
    private String category;

    public MenuItems(String menuName, String price, String imageUrl,String Discription,String category) {
        MenuName = menuName;
        Price = price;
        this.imageUrl = imageUrl;
        this.Discription= Discription;
        this.category = category;

    }

    public String getMenuName() {
        return MenuName;
    }

    public String getPrice() {
        return Price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDiscription() {
        return Discription;
    }

    public String getCategory() {
        return category;
    }
}
