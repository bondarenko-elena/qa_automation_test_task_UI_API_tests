package com.bondarenko.cart.ui_tests;

import com.bondarenko.cart.utils.ui.BaseUiTest;
import com.bondarenko.cart.utils.ui.CartItem;
import org.testng.annotations.Test;

import java.util.ArrayList;


public class CartItemsUiTest extends BaseUiTest  {

    @Test(description = "Adding items through the UI")
    public void addItemsThroughUiTest() {
        ArrayList<CartItem> items = new ArrayList<>();
        items.add(new CartItem("Apple", 10, 2));
        items.add(new CartItem("Banana", 5, 3));
        cartPage.setItemNameFiled(items.get(0).getItemName())
                .setItemPriceFiled(items.get(0).getItemPrice())
                .setItemQuantityFiled(items.get(0).getItemQuantity())
                .clickAddToCartButton()
                .setItemNameFiled(items.get(1).getItemName())
                .setItemPriceFiled(items.get(1).getItemPrice())
                .setItemQuantityFiled(items.get(1).getItemQuantity())
                .clickAddToCartButton()
                .assertCartItemName(items)
                .assertCartItemDetails(items)
                .assertCartItemSubtotal(items);
    }

    @Test(description = "Removing items through the UI")
    public void removeItemsThroughUiTest() {
        ArrayList<CartItem> items = new ArrayList<>();
        items.add(new CartItem("Apple", 10, 2));
        cartPage.setItemNameFiled(items.get(0).getItemName())
                .setItemPriceFiled(items.get(0).getItemPrice())
                .setItemQuantityFiled(items.get(0).getItemQuantity())
                .clickAddToCartButton()
                .clickRemoveButton(1)
                .assertCartIsEmpty();
    }

    @Test(description = "Correct display of prices and totals")
    public void correctPricesAndTotalsTest() {
        ArrayList<CartItem> items = new ArrayList<>();
        items.add(new CartItem("Apple", 10, 2));
        items.add(new CartItem("Banana", 5, 3));
        String subTotalPrice = cartPage.getSubTotalPrice(items);
        String totalPrice = cartPage.getTotalPrice(items, 1);
        cartPage.setItemNameFiled(items.get(0).getItemName())
                .setItemPriceFiled(items.get(0).getItemPrice())
                .setItemQuantityFiled(items.get(0).getItemQuantity())
                .clickAddToCartButton()
                .setItemNameFiled(items.get(1).getItemName())
                .setItemPriceFiled(items.get(1).getItemPrice())
                .setItemQuantityFiled(items.get(1).getItemQuantity())
                .clickAddToCartButton()
                .assertSubtotalPrice(subTotalPrice)
                .assertTotalPrice(totalPrice);
    }
}
