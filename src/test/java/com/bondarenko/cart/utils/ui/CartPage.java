package com.bondarenko.cart.utils.ui;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.testng.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage {

    public CartElements cartElements = new CartElements();

    public CartPage clickAddToCartButton() {
        $(cartElements.getAddToCartButton()).click();
        return this;
    }

    public CartPage setItemNameFiled(String value) {
        $(cartElements.getItemNameFiled()).setValue(value);
        return this;
    }

    public CartPage setItemPriceFiled(int value) {
        $(cartElements.getItemPriceFiled()).setValue(String.valueOf(value));
        return this;
    }

    public CartPage setItemQuantityFiled(int value) {
        $(cartElements.getItemQuantityFiled()).setValue(String.valueOf(value));
        return this;
    }

    public CartPage clearItemQuantityFiled() {
        $(cartElements.getItemQuantityFiled()).setValue("");
        return this;
    }

    public String getFiledErrorMessage(By element) {
        return $(element).getAttribute("validationMessage");
    }

    public CartPage filedShouldHaveErrorMessage(By element) {
        String expectedMessage = "Заповніть це поле.";
        String actualMessage = getFiledErrorMessage(element);
        Assert.assertEquals(actualMessage, expectedMessage);
        return this;
    }

    public CartPage assertCartItemName(ArrayList<CartItem> items) {
        ElementsCollection cartItems = $$(cartElements.getCartItemName());
        for (int i = 0; i < items.size(); i++) {
            cartItems.get(i).shouldHave(exactText(items.get(i).getItemName()));
        }
        return this;
    }

    public CartPage assertCartItemDetails(ArrayList<CartItem> items) {
        ElementsCollection cartItems = $$(cartElements.getCartItemDetails());
        for (int i = 0; i < items.size(); i++) {
            cartItems.get(i).shouldHave(exactText("$" + BigDecimal.valueOf(items.get(i).getItemPrice()).setScale(2) + " x " + items.get(i).getItemQuantity()));
        }
        return this;
    }

    public CartPage assertCartItemSubtotal(ArrayList<CartItem> items) {
        ElementsCollection cartItems = $$(cartElements.getCartItemSubtotal());
        for (int i = 0; i < items.size(); i++) {
            cartItems.get(i).shouldHave(exactText("$" + BigDecimal.valueOf(items.get(i).getItemPrice() * items.get(i).getItemQuantity()).setScale(2)));
        }
        return this;
    }

    public CartPage clickRemoveButton(int itemIndex) {
        $$(cartElements.getRemoveItemButton()).get(itemIndex - 1).click();
        return this;
    }

    public CartPage assertCartIsEmpty() {
        $(cartElements.getEmptyCartMessage()).shouldBe(visible)
                .shouldHave(exactText("Your cart is empty"));
        return this;
    }

    public CartPage assertSubtotalPrice(String value) {
        $(cartElements.getSubtotalPrice()).shouldHave(exactText(String.valueOf(value)));
        return this;
    }

    public CartPage assertTotalPrice(String value) {
        $(cartElements.getTotalPrice()).shouldHave(exactText(String.valueOf(value)));
        return this;
    }

    public String getSubTotalPrice(ArrayList<CartItem> items) {
        int subTotalPrice = 0;
        for (int i = 0; i < items.size(); i++) {
            subTotalPrice += items.get(i).getItemPrice() * items.get(i).getItemQuantity();
        }
        return "$" + BigDecimal.valueOf(subTotalPrice).setScale(2);
    }

    public String getTotalPrice(ArrayList<CartItem> items, int discount) {
        int totalPrice = 0;
        for (int i = 0; i < items.size(); i++) {
            totalPrice += items.get(i).getItemPrice() * items.get(i).getItemQuantity();
        }
        totalPrice = totalPrice * discount;
        return "$" + BigDecimal.valueOf(totalPrice).setScale(2);
    }

    public CartPage setDiscountFiled(String value) {
        $(cartElements.getDiscountFiled()).setValue(value);
        return this;
    }

    public CartPage clickApplyDiscountButton() {
        $(cartElements.getApplyDiscountButton()).click();
        return this;
    }

    public CartPage assertDiscountPrice(String value) {
        $(cartElements.getDiscountPrice()).shouldHave(exactText(value));
        return this;
    }

    public CartPage assertApplyDiscountPopup() {
        Alert alert = Selenide.switchTo().alert();
        String actualText = alert.getText();
        Assert.assertEquals("Discount code applied!", actualText);
        alert.accept();
        return this;
    }
}
