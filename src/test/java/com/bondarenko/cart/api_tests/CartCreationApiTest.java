package com.bondarenko.cart.api_tests;

import com.bondarenko.cart.utils.api.BaseApiTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertTrue;

public class CartCreationApiTest extends BaseApiTest {

    // ---------- Positive cases ----------

    @Test(description = "Cart creation | `/cart` | POST | Create a new cart |")
    public void createCartTest() {
        String cartId = given()
                .when()
                .post("/cart")
                .then()
                .statusCode(201)
                .extract()
                .path("cartId");
        Assert.assertNotNull(cartId);
    }

    @Test(description = "Cart retrieval | `/cart/:cartId` | GET | Get cart details and summary |")
    public void getCartTest() {
        String cartId = createNewCart();
        given()
                .when()
                .get("/cart/" + cartId)
                .then()
                .statusCode(200)
                .body("items.size()", equalTo(0))
                .body("subtotal", equalTo(0))
                .body("discount", equalTo(0))
                .body("total", equalTo(0));
    }

    // ---------- Negative cases ----------

    @Test (description = "Cart retrieval, get cart by invalid cartId | `/cart/:cartId` | GET | Get cart details and summary |")
    public void getCartInvalidIdTest() {
        String invalidCartId = "00000000-0000-0000-0000-000000000000";
        given()
                .when()
                .get("/cart/" + invalidCartId)
                .then()
                .statusCode(404)
                .body("error", equalTo("Cart not found"));
    }

    @Test (description = "Cart retrieval, empty cartId | `/cart/:cartId` | GET | Get cart details and summary |")
    public void getCartEmptyIdTest() {
        String response = given()
                .when()
                .get("/cart/")
                .then()
                .statusCode(404)
                .extract()
                .asString();
        assertTrue(response.contains("Cannot GET /cart/"), "Response does not contain expected message");
    }
}
