package com.bondarenko.cart.ui_tests;

import com.bondarenko.cart.utils.ui.BaseUiTest;
import org.testng.annotations.Test;


public class CartDiscountUiTest extends BaseUiTest  {

    @Test(description = "Applying discount codes through the UI")
    public void setDiscountFieldTest() {
        cartPage.setDiscountFiled("")
                .clickApplyDiscountButton()
                .assertDiscountPrice("-$0.00")
                .setDiscountFiled("SAVE10")
                .clickApplyDiscountButton()
                .assertApplyDiscountPopup();
    }
}
