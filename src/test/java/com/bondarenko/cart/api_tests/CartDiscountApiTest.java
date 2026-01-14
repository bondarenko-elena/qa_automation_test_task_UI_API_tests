package com.bondarenko.cart.api_tests;

import com.bondarenko.cart.utils.api.BaseApiTest;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CartDiscountApiTest extends BaseApiTest {

    @BeforeMethod
    public void createCart() {
        cartId = createNewCart();
    }

    // ---------- Positive cases ----------

    @DataProvider(name = "discountCodes")
    public Object[][] discountCodes() {
        return new Object[][]{
                {"SAVE10", 100, 1, 10},   // code, price, quantity, expected discount
                {"SAVE20", 200, 1, 40},
                {"HALF",   300, 1, 150}
        };
    }

    @Test(description = "Applying valid discount codes | `/cart/:cartId/discount` | POST | Apply discount code |", dataProvider = "discountCodes")
    public void applyDiscountTest(String code, int price, int quantity, int expectedDiscount) {
        addItem(cartId, String.format("{\"name\":\"Item\",\"price\":%d,\"quantity\":%d}", price, quantity), 201);
        applyDiscount(cartId, code, 200);
        given()
                .when()
                .get("/cart/" + cartId)
                .then()
                .statusCode(200)
                .body("subtotal", equalTo(price * quantity))
                .body("discount", equalTo(expectedDiscount))
                .body("total", equalTo(price * quantity - expectedDiscount));
    }

    // ---------- Negative cases ----------

    @DataProvider(name = "invalidDiscountCodes")
    public Object[][] invalidDiscountCodes() {
        return new Object[][]{
                {"INVALID_CODE"},
                {""},
                {null},
                {"123"}  // wrong type code
        };
    }

    @Test(description = "Applying invalid discount codes | `/cart/:cartId/discount` | POST | Apply discount code |", dataProvider = "invalidDiscountCodes")
    public void applyInvalidDiscountCodeTest(String code) {
        given()
                .contentType(ContentType.JSON)
                .body(String.format("{\"code\":%s}", code == null ? "null" : "\"" + code + "\""))
                .when()
                .post(String.format(DISCOUNT_ENDPOINT, cartId))
                .then()
                .statusCode(400);
    }

    @Test(description = "Applying invalid discount codes | `/cart/:cartId/discount` | POST | Apply discount code |")
    public void applyDiscountToNonExistingCartTest() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"code\":\"SAVE10\"}")
                .when()
                .post("/cart/00000000-0000-0000-0000-000000000000/discount")
                .then()
                .statusCode(404);
    }

    // ---------- Discount calculation accuracy ----------

    @Test(description = "Extreme subtotal value")
    public void applyDiscountLargeSubtotalTest() {
        addItem(cartId, "{\"name\":\"ExpensiveItem\",\"price\":1000000,\"quantity\":2}", 201);
        applyDiscount(cartId, "HALF", 200);
        given()
                .when()
                .get("/cart/" + cartId)
                .then()
                .statusCode(200)
                .body("subtotal", equalTo(2000000))
                .body("discount", equalTo(1000000))
                .body("total", equalTo(1000000));
    }

    @DataProvider(name = "discountScenarios")
    public Object[][] discountScenarios() {
        return new Object[][]{
                {
                        new String[]{
                                "{\"name\":\"Apple\",\"price\":100,\"quantity\":1}",
                                "{\"name\":\"Banana\",\"price\":50,\"quantity\":2}"
                        },
                        "SAVE10",
                        200,
                        20,
                        180
                },
                {
                        new String[]{
                                "{\"name\":\"Apple\",\"price\":33,\"quantity\":3}",
                                "{\"name\":\"Banana\",\"price\":25,\"quantity\":2}"
                        },
                        "HALF",
                        149,
                        74,
                        75
                },
                {
                        new String[]{
                                "{\"name\":\"Apple\",\"price\":50,\"quantity\":2}",
                                "{\"name\":\"Banana\",\"price\":25,\"quantity\":4}"
                        },
                        "SAVE20",
                        200,
                        40,
                        160
                }
        };
    }

    @Test(description = "Discount calculation: recalculation, boundary values and multiple items", dataProvider = "discountScenarios")
    public void discountScenarioTest(String[] items, String code, int expectedSubtotal, int expectedDiscount, int expectedTotal) {
        for (String itemJson : items) {
            addItem(cartId, itemJson, 201);
        }
        applyDiscount(cartId, code, 200);
        given()
                .when()
                .get("/cart/" + cartId)
                .then()
                .statusCode(200)
                .body("subtotal", equalTo(expectedSubtotal))
                .body("discount", equalTo(expectedDiscount))
                .body("total", equalTo(expectedTotal));
    }
}
