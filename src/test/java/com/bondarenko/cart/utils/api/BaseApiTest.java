package com.bondarenko.cart.utils.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;

public class BaseApiTest {

    protected static final String CART_ITEMS_ENDPOINT = "/cart/%s/items";
    protected static final String CART_ITEM_ENDPOINT = "/cart/%s/items/%s";
    protected static final String DISCOUNT_ENDPOINT = "/cart/%s/discount";
    protected String cartId;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    public String createNewCart() {
        return given()
                .when()
                .post("/cart")
                .then()
                .extract()
                .path("cartId");
    }

    public String addItem(String cartId, String body, int expectedStatus) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(String.format(CART_ITEMS_ENDPOINT, cartId))
                .then()
                .statusCode(expectedStatus)
                .extract()
                .path("id");
    }

    public void addItemExpectStatus(String cartId, String body, int expectedStatus) {
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(String.format(CART_ITEMS_ENDPOINT, cartId))
                .then()
                .statusCode(expectedStatus);
    }

    public void applyDiscount(String cartId, String code, int expectedStatus) {
        given()
                .contentType(ContentType.JSON)
                .body("{\"code\":\"" + code + "\"}")
                .when()
                .post(String.format(DISCOUNT_ENDPOINT, cartId))
                .then()
                .statusCode(expectedStatus);
    }
}