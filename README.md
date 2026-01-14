# How to Run the Tests

The project contains automated tests organized as follows:

- `src/test/java/com/bondarenko/cart/api_tests/` — API tests written in Java using **RestAssured**
- `src/test/java/com/bondarenko/cart/ui_tests/` — UI tests written in Java using **Selenide**

---

## Ways to Run the Tests

### Option 1 — Run tests manually from IntelliJ IDEA
1. Open any test class from either `api_tests` or `ui_tests` folder
2. Right-click on the class or a specific test method
3. Click **Run**

### Option 2 — Run tests using TestNG suites
The project includes two pre-configured **TestNG** test suites:

- **API tests suite:** `src/test/resources/cart/api_test_suite.xml`
- **UI tests suite:** `src/test/resources/cart/ui_test_suite.xml`

To run a suite:

1. Open the corresponding `.xml` file in IntelliJ
2. Right-click → **Run**

---

## Headless Mode for UI Tests

UI tests can be executed in **headless mode** (without opening a browser window).

- To enable headless mode: open `src/test/resources/selenide.properties` and set:
