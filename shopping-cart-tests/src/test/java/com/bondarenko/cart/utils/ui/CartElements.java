package com.bondarenko.cart.utils.ui;

import org.openqa.selenium.By;

public class CartElements {

    public By getAddToCartButton() {
        return By.xpath("//button[text()='Add to Cart']");
    }

    public By getItemNameFiled() {
        return By.id("itemName");
    }

    public By getItemPriceFiled() {
        return By.id("itemPrice");
    }

    public By getItemQuantityFiled() {
        return By.id("itemQuantity");
    }

    public By getCartItemName() {
        return By.className("cart-item-name");
    }

    public By getCartItemDetails() {
        return By.className("cart-item-details");
    }

    public By getCartItemSubtotal() {
        return By.className("cart-item-subtotal");
    }

    public By getRemoveItemButton() {
        return By.xpath("//button[text()='Remove']");
    }

    public By getEmptyCartMessage() {
        return By.className("empty-cart");
    }

    public By getSubtotalPrice() {
        return By.id("subtotal");
    }

    public By getTotalPrice() {
        return By.id("total");
    }

    public By getDiscountFiled() {
        return By.id("discountCode");
    }

    public By getApplyDiscountButton() {
        return By.id("applyDiscount");
    }

    public By getDiscountPrice() {
        return By.id("discount");
    }
}
