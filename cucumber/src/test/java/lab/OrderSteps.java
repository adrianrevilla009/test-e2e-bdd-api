package lab;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Step definitions; Cucumber creates one instance per scenario, so state never leaks between scenarios. */
public class OrderSteps {
    private final HttpClient http = HttpClient.newHttpClient();
    private OrdersStub stub;
    private String base;
    private HttpResponse<String> response;
    private int orderId;

    @Before
    public void startApi() throws Exception {
        stub = new OrdersStub();
        base = "http://127.0.0.1:" + stub.start();
    }

    @After
    public void stopApi() { stub.stop(); }

    @Given("I have placed an order for {int} of {string}")
    public void iHavePlaced(int qty, String sku) throws Exception {
        iPlace(qty, sku);
        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(response.body());
        assertTrue(m.find(), response.body());
        orderId = Integer.parseInt(m.group(1));
    }

    @When("I place an order for {int} of {string}")
    public void iPlace(int qty, String sku) throws Exception {
        String body = "{\"sku\":\"" + sku + "\",\"qty\":" + qty + "}";
        response = http.send(HttpRequest.newBuilder(URI.create(base + "/orders"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(body)).build(), HttpResponse.BodyHandlers.ofString());
    }

    @When("I look the order up")
    public void iLookUp() throws Exception { iLookUpId(orderId); }

    @When("I look up order {int}")
    public void iLookUpId(int id) throws Exception {
        response = http.send(HttpRequest.newBuilder(URI.create(base + "/orders/" + id)).GET().build(),
            HttpResponse.BodyHandlers.ofString());
    }

    @Then("the response status is {int}")
    public void status(int expected) { assertEquals(expected, response.statusCode()); }

    @Then("the order status is {string}")
    public void orderStatus(String expected) { assertTrue(response.body().contains("\"status\":\"" + expected + "\""), response.body()); }

    @Then("the order quantity is {int}")
    public void orderQty(int expected) { assertTrue(response.body().contains("\"qty\":" + expected), response.body()); }
}
