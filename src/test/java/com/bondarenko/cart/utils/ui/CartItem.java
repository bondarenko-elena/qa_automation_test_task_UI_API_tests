package com.bondarenko.cart.utils.ui;

public class CartItem {

    private String itemName;
    private int itemPrice;
    private int itemQuantity;

    public CartItem(String itemName, int itemPrice, int itemQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }

    public String getItemName() {
        return itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemName(String name) {
        itemName = name;
    }

    public void setItemPrice(int price) {
        itemPrice = price;
    }

    public void setItemQuantity(int quantity) {
        itemQuantity = quantity;
    }
}
