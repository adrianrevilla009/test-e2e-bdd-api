package lab;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.junit.jupiter.api.Test;

class OrdersKarateTest {
    @Test
    void runFeatures() throws Exception {
        OrdersStub stub = new OrdersStub();
        System.setProperty("orders.port", String.valueOf(stub.start()));
        try {
            Results results = Runner.path("classpath:lab").parallel(1);
            assertEquals(0, results.getFailCount(), results.getErrorMessages());
            assertEquals(4, results.getScenariosPassed(), "all four scenarios must run");
        } finally {
            stub.stop();
        }
    }
}
