    package com.bondarenko.cart.api_tests;

    import com.bondarenko.cart.utils.api.BaseApiTest;
    import io.restassured.http.ContentType;
    import org.testng.annotations.BeforeMethod;
    import org.testng.annotations.Test;

    import static com.codeborne.selenide.Selenide.open;
    import static io.restassured.RestAssured.given;
    import static io.restassured.RestAssured.when;
    import static org.hamcrest.Matchers.*;

    public class CartItemsApiTest extends BaseApiTest {

        @BeforeMethod
        public void createCart() {
            cartId = createNewCart();
        }

        // ---------- Positive cases ----------

        @Test(description = "Adding items with valid data | `/cart/:cartId/items` | POST | Add item to cart |")
        public void addItemValidDataTest() {
            String itemId = addItem(
                    cartId,
                    "{\"name\":\"Apple\",\"price\":10,\"quantity\":2}",
                    201
            );
            given()
                    .get("/cart/" + cartId)
                    .then()
                    .body("items[0].id", equalTo(itemId))
                    .body("items[0].name", equalTo("Apple"))
                    .body("items[0].price", equalTo(10))
                    .body("items[0].quantity", equalTo(2));
        }

        @Test(description = "Adding items with valid data | `/cart/:cartId/items` | POST | Add item to cart |")
        public void addMultipleItemsValidDataTest() {
            String itemId1 = addItem(cartId, "{\"name\":\"Apple\",\"price\":10,\"quantity\":2}", 201);
            String itemId2 = addItem(cartId, "{\"name\":\"Banana\",\"price\":5,\"quantity\":3}", 201);
            String itemId3 = addItem(cartId, "{\"name\":\"Orange\",\"price\":8,\"quantity\":1}", 201);
            given()
                    .get("/cart/" + cartId)
                    .then()
                    .body("items.size()", equalTo(3))
                    .body("items.find { it.id == '" + itemId1 + "' }.name", equalTo("Apple"))
                    .body("items.find { it.id == '" + itemId2 + "' }.name", equalTo("Banana"))
                    .body("items.find { it.id == '" + itemId3 + "' }.name", equalTo("Orange"));
        }

        @Test(description = "Removing items from cart | `/cart/:cartId/items/:itemId` | DELETE | Remove item from cart |")
        public void removeItemTest() {
            String itemId = addItem(
                    cartId,
                    "{\"name\":\"Banana\",\"price\":5,\"quantity\":3}",
                    201
            );
            when()
                    .delete(String.format(CART_ITEM_ENDPOINT, cartId, itemId))
                    .then()
                    .statusCode(204);
            given()
                    .get("/cart/" + cartId)
                    .then()
                    .body("items.size()", equalTo(0));
        }

        // ---------- Negative cases ----------

        @Test(description = "Adding items with invalid data  | `/cart/:cartId/items` | POST | Add item to cart |")
        public void addItemNegativePriceTest() {
            addItemExpectStatus(
                    cartId,
                    "{\"name\":\"Apple\",\"price\":-10,\"quantity\":2}",
                    400
            );
        }

        @Test(description = "Adding items with invalid data  | `/cart/:cartId/items` | POST | Add item to cart |")
        public void addItemZeroQuantityTest() {
            addItemExpectStatus(
                    cartId,
                    "{\"name\":\"Apple\",\"price\":10,\"quantity\":0}",
                    400
            );
        }

        @Test(description = "Adding items with invalid data  | `/cart/:cartId/items` | POST | Add item to cart |")
        public void addItemMissingFieldsTest() {
            addItemExpectStatus(cartId, "{\"price\":10,\"quantity\":2}", 400);
            addItemExpectStatus(cartId, "{\"name\":\"Apple\",\"quantity\":2}", 400);
            addItemExpectStatus(cartId, "{\"name\":\"Apple\",\"price\":10}", 400);
        }

        @Test(description = "Adding items with invalid data  | `/cart/:cartId/items` | POST | Add item to cart |")
        public void addItemToNonExistingCartTest() {
            given()
                    .contentType(ContentType.JSON)
                    .body("{\"name\":\"Apple\",\"price\":10,\"quantity\":1}")
                    .post("/cart/00000000-0000-0000-0000-000000000000/items")
                    .then()
                    .statusCode(404);
        }

        @Test(description = "Adding items with invalid data  | `/cart/:cartId/items` | POST | Add item to cart |")
        public void addItemInvalidDataTypesTest() {
            addItemExpectStatus(cartId, "{\"name\":\"Apple\",\"price\":\"ten\",\"quantity\":2}", 400);
            addItemExpectStatus(cartId, "{\"name\":\"Apple\",\"price\":10,\"quantity\":\"two\"}", 400);
            addItemExpectStatus(cartId, "{\"name\":123,\"price\":10,\"quantity\":2}", 400);
        }

        @Test(description = "Removing items with invalid data | `/cart/:cartId/items/:itemId` | DELETE | Remove item from cart |")
        public void removeNonExistingItemTest() {
            when()
                    .delete(String.format(CART_ITEM_ENDPOINT, cartId, "invalid-item-id"))
                    .then()
                    .statusCode(404);
        }

        @Test(description = "Removing items with invalid data | `/cart/:cartId/items/:itemId` | DELETE | Remove item from cart |")
        public void removeSameItemTwiceTest() {
            String itemId = addItem(cartId, "{\"name\":\"Apple\",\"price\":10,\"quantity\":1}", 201
            );
            when()
                    .delete(String.format(CART_ITEM_ENDPOINT, cartId, itemId))
                    .then()
                    .statusCode(204);
            when()
                    .delete(String.format(CART_ITEM_ENDPOINT, cartId, itemId))
                    .then()
                    .statusCode(404);
        }

        @Test
        public void cartTotalsAfterAddAndRemoveTest() {
            addItem(cartId, "{\"name\":\"Apple\",\"price\":10,\"quantity\":2}", 201);
            String bananaId = addItem(cartId, "{\"name\":\"Banana\",\"price\":5,\"quantity\":3}", 201);
            given()
                    .get("/cart/" + cartId)
                    .then()
                    .body("subtotal", equalTo(35))
                    .body("total", equalTo(35));
            when()
                    .delete(String.format(CART_ITEM_ENDPOINT, cartId, bananaId))
                    .then()
                    .statusCode(204);
            given()
                    .get("/cart/" + cartId)
                    .then()
                    .body("subtotal", equalTo(20))
                    .body("total", equalTo(20));
        }
    }
