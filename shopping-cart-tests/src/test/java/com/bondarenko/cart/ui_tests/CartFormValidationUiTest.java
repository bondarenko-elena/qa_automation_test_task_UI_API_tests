package com.bondarenko.cart.ui_tests;

import com.bondarenko.cart.utils.ui.BaseUiTest;
import org.testng.annotations.Test;


public class CartFormValidationUiTest extends BaseUiTest {

    @Test(description = "Form validation")
    public void formValidationTest() {
        cartPage.clickAddToCartButton()
                .filedShouldHaveErrorMessage(cartElements.getItemNameFiled())
                .clickAddToCartButton()
                .filedShouldHaveErrorMessage(cartElements.getItemPriceFiled())
                .clearItemQuantityFiled()
                .clickAddToCartButton()
                .filedShouldHaveErrorMessage(cartElements.getItemQuantityFiled());
    }
}
