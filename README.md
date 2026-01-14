How to run the tests

The project contains automated tests organized as follows:
	- src/test/java/com/bondarenko/cart/api_tests/ — API tests written in Java using RestAssured
	- src/test/java/com/bondarenko/cart/ui_tests/ — UI tests written in Java using Selenide

You can run the tests in one of the following ways:
Option 1 — Run tests manually from IntelliJ IDEA
	- Open any test class from either api_tests or ui_tests folder
	- Right-click on the class or a specific test method
	- Click Run

Option 2 — Run tests using TestNG suites
The project includes two pre-configured TestNG test suites:
	- API tests suite: src/test/resources/cart/api_test_suite.xml
	- UI tests suite: src/test/resources/cart/ui_test_suite.xml

To run a suite:
	- Open the corresponding .xml file in IntelliJ
	- Right-click → Run

UI tests can be executed in headless mode (without opening a browser window).
To enable it: open: src/test/resources/selenide.properties and set [selenide.headless=true]
To run tests with a visible browser, set [selenide.headless=false]
