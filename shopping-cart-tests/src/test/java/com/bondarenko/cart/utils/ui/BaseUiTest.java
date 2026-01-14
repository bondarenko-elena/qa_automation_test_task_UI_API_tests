package com.bondarenko.cart.utils.ui;

import com.bondarenko.cart.utils.api.BaseApiTest;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static com.codeborne.selenide.Selenide.open;

public class BaseUiTest {

    protected CartPage cartPage = new CartPage();
    protected CartElements cartElements = new CartElements();

    @BeforeClass
    public void createCart() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
        BaseApiTest baseApiTest = new BaseApiTest();
        baseApiTest.createNewCart();
    }

    @BeforeMethod
    public void openApp() {
        open("http://localhost:3000");
    }
}
