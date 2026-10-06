package lab;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Tiny in-memory Orders API: POST /orders, GET /orders/{id}. Port 0 = free port, so tests never collide. */
public class OrdersStub {
    private final HttpServer server;
    private final Map<Integer, String> orders = new ConcurrentHashMap<>();
    private final AtomicInteger ids = new AtomicInteger();

    public OrdersStub() throws IOException {
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/orders", this::handle);
    }

    public int start() { server.start(); return server.getAddress().getPort(); }
    public void stop() { server.stop(0); }

    private void handle(HttpExchange ex) throws IOException {
        String path = ex.getRequestURI().getPath();
        if (ex.getRequestMethod().equals("POST") && path.equals("/orders")) {
            String body = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Matcher qty = Pattern.compile("\"qty\"\\s*:\\s*(-?\\d+)").matcher(body);
            if (!body.contains("\"sku\"") || !qty.find() || Integer.parseInt(qty.group(1)) <= 0) {
                send(ex, 400, "{\"error\":\"invalid order\"}");
                return;
            }
            int id = ids.incrementAndGet();
            String json = "{\"id\":" + id + ",\"status\":\"NEW\",\"qty\":" + qty.group(1) + "}";
            orders.put(id, json);
            send(ex, 201, json);
        } else if (ex.getRequestMethod().equals("GET") && path.matches("/orders/\\d+")) {
            String json = orders.get(Integer.parseInt(path.substring(8)));
            if (json == null) send(ex, 404, "{\"error\":\"not found\"}"); else send(ex, 200, json);
        } else {
            send(ex, 405, "{\"error\":\"unsupported\"}");
        }
    }

    private void send(HttpExchange ex, int code, String json) throws IOException {
        byte[] b = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().add("Content-Type", "application/json");
        ex.sendResponseHeaders(code, b.length);
        ex.getResponseBody().write(b);
        ex.close();
    }
}
