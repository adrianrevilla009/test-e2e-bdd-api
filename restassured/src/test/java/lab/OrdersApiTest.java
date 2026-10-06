package lab;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class OrdersApiTest {
    static OrdersStub stub;

    @BeforeAll
    static void up() throws Exception {
        stub = new OrdersStub();
        RestAssured.baseURI = "http://127.0.0.1";
        RestAssured.port = stub.start();
    }

    @AfterAll
    static void down() { stub.stop(); }

    // Test data strategy: every test creates its own order, none relies on shared rows.
    private int createOrder(String sku, int qty) {
        return given().contentType(ContentType.JSON).body("{\"sku\":\"" + sku + "\",\"qty\":" + qty + "}")
            .post("/orders").then().statusCode(201).extract().path("id");
    }

    @Test
    void createsOrder() {
        given().contentType(ContentType.JSON).body("{\"sku\":\"ABC\",\"qty\":2}")
            .when().post("/orders")
            .then().statusCode(201).body("id", greaterThan(0)).body("status", equalTo("NEW"));
    }

    @Test
    void fetchesCreatedOrder() {
        int id = createOrder("XYZ", 3);
        given().when().get("/orders/{id}", id)
            .then().statusCode(200).body("qty", equalTo(3));
    }

    @Test
    void rejectsInvalidQuantity() {
        given().contentType(ContentType.JSON).body("{\"sku\":\"ABC\",\"qty\":0}")
            .when().post("/orders").then().statusCode(400);
    }

    @Test
    void unknownOrderIs404() {
        given().when().get("/orders/99999").then().statusCode(404);
    }
}
