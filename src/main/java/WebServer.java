import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

/**
 * Embedded HTTP server for Byte Me! web UI.
 * Serves static files from src/main/java/web/ and exposes a JSON API.
 */
public class WebServer {

    private final HttpServer server;
    private static final String WEB_DIR = "src/main/java/web";

    public WebServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api/menu",       new MenuHandler());
        server.createContext("/api/orders",     new OrdersHandler());
        server.createContext("/api/customers",  new CustomersHandler());
        server.createContext("/api/cart",       new CartHandler());
        server.createContext("/api/checkout",   new CheckoutHandler());
        server.createContext("/api/admin",      new AdminHandler());
        server.createContext("/api/report",     new ReportHandler());
        server.createContext("/",               new StaticHandler());
        server.setExecutor(null);
    }

    public void start() {
        server.start();
        System.out.println("Byte Me! web UI running at http://localhost:" + server.getAddress().getPort());
    }

    public void stop() {
        server.stop(0);
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private static void sendJson(HttpExchange ex, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
    }

    private static void sendError(HttpExchange ex, int status, String msg) throws IOException {
        sendJson(ex, status, "{\"error\":\"" + escJson(msg) + "\"}");
    }

    private static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new LinkedHashMap<>();
        if (query == null || query.isEmpty()) return map;
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            try {
                String key = URLDecoder.decode(kv[0], "UTF-8");
                String val = kv.length > 1 ? URLDecoder.decode(kv[1], "UTF-8") : "";
                map.put(key, val);
            } catch (Exception ignored) {}
        }
        return map;
    }

    private static String readBody(HttpExchange ex) throws IOException {
        return new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private static Map<String, String> parseJsonSimple(String json) {
        Map<String, String> map = new LinkedHashMap<>();
        json = json.trim().replaceAll("^\\{|\\}$", "");
        for (String entry : json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")) {
            String[] kv = entry.split(":", 2);
            if (kv.length == 2) {
                String k = kv[0].trim().replaceAll("^\"|\"$", "");
                String v = kv[1].trim().replaceAll("^\"|\"$", "");
                map.put(k, v);
            }
        }
        return map;
    }

    private static String escJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }

    private static Customer findCustomer(String name) {
        for (Customer c : Database.customers)
            if (c.getName().equalsIgnoreCase(name)) return c;
        for (Customer c : Database.VIP)
            if (c.getName().equalsIgnoreCase(name)) return c;
        return null;
    }

    // ── /api/menu ────────────────────────────────────────────────────────────

    static class MenuHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if ("OPTIONS".equals(ex.getRequestMethod())) { sendJson(ex, 200, "{}"); return; }
            if ("GET".equals(ex.getRequestMethod())) {
                StringBuilder sb = new StringBuilder("[");
                for (int i = 0; i < Menu.items.size(); i++) {
                    Food f = Menu.items.get(i);
                    if (i > 0) sb.append(",");
                    sb.append("{")
                      .append("\"name\":\"").append(escJson(f.getName())).append("\",")
                      .append("\"price\":").append(f.getPrice()).append(",")
                      .append("\"available\":").append(f.isAvailable()).append(",")
                      .append("\"category\":\"").append(escJson(f.getCategory())).append("\"")
                      .append("}");
                }
                sb.append("]");
                sendJson(ex, 200, sb.toString());
                return;
            }
            if ("POST".equals(ex.getRequestMethod())) {
                Map<String, String> body = parseJsonSimple(readBody(ex));
                String name = body.get("name");
                String cat  = body.get("category");
                double price;
                try { price = Double.parseDouble(body.getOrDefault("price","0")); }
                catch (NumberFormatException e) { sendError(ex, 400, "invalid price"); return; }
                boolean avail = Boolean.parseBoolean(body.getOrDefault("available","true"));
                if (name == null || name.isEmpty() || cat == null) {
                    sendError(ex, 400, "name and category required"); return;
                }
                Food item = new Food(name, price, avail, cat);
                Menu.items.add(item);
                Menu.Prices.add(price);
                if (cat.equalsIgnoreCase("Snacks")) Menu.Snacks.add(item);
                else if (cat.equalsIgnoreCase("Drinks")) Menu.Drinks.add(item);
                else if (cat.equalsIgnoreCase("Meals")) Menu.Meals.add(item);
                sendJson(ex, 201, "{\"status\":\"added\"}");
                return;
            }
            if ("DELETE".equals(ex.getRequestMethod())) {
                Map<String, String> q = parseQuery(ex.getRequestURI().getQuery());
                String name = q.get("name");
                Food toRemove = null;
                for (Food f : Menu.items) if (f.getName().equalsIgnoreCase(name)) { toRemove = f; break; }
                if (toRemove == null) { sendError(ex, 404, "item not found"); return; }
                Menu.items.remove(toRemove);
                Menu.Snacks.remove(toRemove); Menu.Drinks.remove(toRemove); Menu.Meals.remove(toRemove);
                sendJson(ex, 200, "{\"status\":\"removed\"}");
                return;
            }
            if ("PATCH".equals(ex.getRequestMethod())) {
                Map<String, String> body = parseJsonSimple(readBody(ex));
                String name = body.get("name");
                for (Food f : Menu.items) {
                    if (f.getName().equalsIgnoreCase(name)) {
                        if (body.containsKey("price")) f.setPrice(Double.parseDouble(body.get("price")));
                        if (body.containsKey("available")) f.setAvailability(Boolean.parseBoolean(body.get("available")));
                        if (body.containsKey("category")) f.setCategory(body.get("category"));
                        sendJson(ex, 200, "{\"status\":\"updated\"}");
                        return;
                    }
                }
                sendError(ex, 404, "item not found");
            }
        }
    }

    // ── /api/orders ──────────────────────────────────────────────────────────

    static class OrdersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if ("GET".equals(ex.getRequestMethod())) {
                Map<String, String> q = parseQuery(ex.getRequestURI().getQuery());
                String customer = q.get("customer");
                StringBuilder sb = new StringBuilder("{\"vip\":{");
                boolean first = true;
                for (Map.Entry<String, ArrayList<Food>> e : Database.VIP_Order.entrySet()) {
                    if (customer != null && !e.getKey().equalsIgnoreCase(customer)) continue;
                    if (!first) sb.append(",");
                    first = false;
                    sb.append("\"").append(escJson(e.getKey())).append("\":[");
                    appendFoodList(sb, e.getValue());
                    sb.append("]");
                }
                sb.append("},\"regular\":{");
                first = true;
                for (Map.Entry<String, ArrayList<Food>> e : Database.Order.entrySet()) {
                    if (customer != null && !e.getKey().equalsIgnoreCase(customer)) continue;
                    if (!first) sb.append(",");
                    first = false;
                    sb.append("\"").append(escJson(e.getKey())).append("\":[");
                    appendFoodList(sb, e.getValue());
                    sb.append("]");
                }
                sb.append("}}");
                sendJson(ex, 200, sb.toString());
            }
        }

        private void appendFoodList(StringBuilder sb, ArrayList<Food> items) {
            for (int i = 0; i < items.size(); i++) {
                Food f = items.get(i);
                if (i > 0) sb.append(",");
                sb.append("{\"name\":\"").append(escJson(f.getName())).append("\",")
                  .append("\"qty\":").append(f.getQuantity()).append(",")
                  .append("\"price\":").append(f.getPrice()).append(",")
                  .append("\"status\":\"").append(escJson(f.getStatus())).append("\",")
                  .append("\"special\":\"").append(escJson(f.getSpecialreq())).append("\"")
                  .append("}");
            }
        }
    }

    // ── /api/customers ───────────────────────────────────────────────────────

    static class CustomersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if ("GET".equals(ex.getRequestMethod())) {
                StringBuilder sb = new StringBuilder("[");
                boolean first = true;
                for (Customer c : Database.customers) {
                    if (!first) sb.append(",");
                    first = false;
                    sb.append("{\"name\":\"").append(escJson(c.getName()))
                      .append("\",\"roll\":").append(c.getRoll_number())
                      .append(",\"vip\":false}");
                }
                for (Customer c : Database.VIP) {
                    if (!first) sb.append(",");
                    first = false;
                    sb.append("{\"name\":\"").append(escJson(c.getName()))
                      .append("\",\"roll\":").append(c.getRoll_number())
                      .append(",\"vip\":true}");
                }
                sb.append("]");
                sendJson(ex, 200, sb.toString());
            }
            if ("POST".equals(ex.getRequestMethod())) {
                Map<String, String> body = parseJsonSimple(readBody(ex));
                String name = body.get("name");
                int roll;
                try { roll = Integer.parseInt(body.getOrDefault("roll","0")); }
                catch (NumberFormatException e) { sendError(ex, 400, "invalid roll number"); return; }
                boolean vip = Boolean.parseBoolean(body.getOrDefault("vip","false"));
                if (findCustomer(name) != null) { sendError(ex, 409, "customer already exists"); return; }
                new Customer(name, roll, vip);
                sendJson(ex, 201, "{\"status\":\"created\"}");
            }
        }
    }

    // ── /api/cart ────────────────────────────────────────────────────────────

    static class CartHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Map<String, String> q = parseQuery(ex.getRequestURI().getQuery());
            String customerName = q.get("customer");
            Customer customer = findCustomer(customerName);
            if (customer == null) { sendError(ex, 404, "customer not found"); return; }

            if ("GET".equals(ex.getRequestMethod())) {
                StringBuilder sb = new StringBuilder("{\"items\":[");
                for (int i = 0; i < customer.Cart.size(); i++) {
                    Food f = customer.Cart.get(i);
                    if (i > 0) sb.append(",");
                    sb.append("{\"name\":\"").append(escJson(f.getName())).append("\",")
                      .append("\"qty\":").append(f.getQuantity()).append(",")
                      .append("\"price\":").append(f.getPrice()).append(",")
                      .append("\"special\":\"").append(escJson(f.getSpecialreq())).append("\"")
                      .append("}");
                }
                sb.append("],\"total\":").append(customer.getTotalPrice()).append("}");
                sendJson(ex, 200, sb.toString());
                return;
            }
            if ("POST".equals(ex.getRequestMethod())) {
                Map<String, String> body = parseJsonSimple(readBody(ex));
                String itemName = body.get("item");
                int qty;
                try { qty = Integer.parseInt(body.getOrDefault("qty","1")); }
                catch (NumberFormatException e) { sendError(ex, 400, "invalid quantity"); return; }
                String special = body.getOrDefault("special", null);
                customer.addToCart(itemName, qty, special);
                sendJson(ex, 200, "{\"status\":\"added\",\"total\":" + customer.getTotalPrice() + "}");
                return;
            }
            if ("DELETE".equals(ex.getRequestMethod())) {
                String itemName = q.get("item");
                customer.removeItem(itemName);
                sendJson(ex, 200, "{\"status\":\"removed\",\"total\":" + customer.getTotalPrice() + "}");
            }
        }
    }

    // ── /api/checkout ─────────────────────────────────────────────────────────

    static class CheckoutHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            if (!"POST".equals(ex.getRequestMethod())) { sendError(ex, 405, "POST only"); return; }
            Map<String, String> body = parseJsonSimple(readBody(ex));
            String customerName = body.get("customer");
            String address      = body.getOrDefault("address", "Canteen");
            String payment      = body.getOrDefault("payment", "Cash");

            Customer customer = findCustomer(customerName);
            if (customer == null) { sendError(ex, 404, "customer not found"); return; }
            if (customer.Cart.isEmpty()) { sendError(ex, 400, "cart is empty"); return; }

            for (Food f : customer.Cart) {
                f.setCustomer_name(customer.getName());
                f.setDelivery_address(address);
            }

            Map<String, ArrayList<Food>> orderMap = customer.isVIP() ? Database.VIP_Order : Database.Order;
            ArrayList<Food> cartCopy = new ArrayList<>(customer.Cart);

            if (orderMap.containsKey(customer.getName())) {
                orderMap.get(customer.getName()).addAll(cartCopy);
            } else {
                orderMap.put(customer.getName(), cartCopy);
            }

            for (Food f : cartCopy) {
                Database.foodFrequency.merge(f.getName(), f.getQuantity(), Integer::sum);
            }
            Database.orderfreq++;
            customer.Order_history.addAll(cartCopy);
            customer.Cart.clear();

            sendJson(ex, 200, "{\"status\":\"placed\",\"vip\":" + customer.isVIP() + "}");
        }
    }

    // ── /api/admin ────────────────────────────────────────────────────────────

    static class AdminHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            Map<String, String> q = parseQuery(ex.getRequestURI().getQuery());
            String action = q.getOrDefault("action", "");

            if ("processVip".equals(action)) {
                int processed = 0;
                for (Iterator<Map.Entry<String, ArrayList<Food>>> it = Database.VIP_Order.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<String, ArrayList<Food>> entry = it.next();
                    for (Food f : entry.getValue()) {
                        f.setStatus("Out for Delivery");
                        Database.Daily_sales.add(f);
                    }
                    it.remove();
                    processed++;
                }
                sendJson(ex, 200, "{\"processed\":" + processed + "}");
                return;
            }
            if ("processRegular".equals(action)) {
                if (!Database.VIP_Order.isEmpty()) {
                    sendError(ex, 400, "Process all VIP orders first"); return;
                }
                int processed = 0;
                for (Iterator<Map.Entry<String, ArrayList<Food>>> it = Database.Order.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<String, ArrayList<Food>> entry = it.next();
                    for (Food f : entry.getValue()) {
                        f.setStatus("Out for Delivery");
                        Database.Daily_sales.add(f);
                    }
                    it.remove();
                    processed++;
                }
                sendJson(ex, 200, "{\"processed\":" + processed + "}");
                return;
            }
            if ("cancelOrder".equals(action)) {
                String customer = q.get("customer");
                if (Database.VIP_Order.containsKey(customer)) {
                    Database.Cancelled_Order.addAll(Database.VIP_Order.remove(customer));
                } else if (Database.Order.containsKey(customer)) {
                    Database.Cancelled_Order.addAll(Database.Order.remove(customer));
                } else {
                    sendError(ex, 404, "no pending order for this customer"); return;
                }
                sendJson(ex, 200, "{\"status\":\"cancelled\"}");
                return;
            }
            if ("newDay".equals(action)) {
                Database.Daily_sales.clear();
                sendJson(ex, 200, "{\"status\":\"day reset\"}");
                return;
            }
            sendError(ex, 400, "unknown action");
        }
    }

    // ── /api/report ───────────────────────────────────────────────────────────

    static class ReportHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            double total = 0;
            for (Food f : Database.Daily_sales) total += f.getPrice() * f.getQuantity();

            String popular = "None";
            int max = 0;
            for (Map.Entry<String, Integer> e : Database.foodFrequency.entrySet()) {
                if (e.getValue() > max) { max = e.getValue(); popular = e.getKey(); }
            }

            sendJson(ex, 200,
                "{\"dailySales\":" + total +
                ",\"totalOrders\":" + Database.orderfreq +
                ",\"popularItem\":\"" + escJson(popular) + "\"" +
                ",\"popularCount\":" + max + "}");
        }
    }

    // ── static file handler ───────────────────────────────────────────────────

    static class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            String path = ex.getRequestURI().getPath();
            if (path.equals("/")) path = "/index.html";
            // Security: prevent directory traversal
            if (path.contains("..")) { sendError(ex, 403, "forbidden"); return; }

            File file = new File(WEB_DIR + path);
            if (!file.exists() || file.isDirectory()) {
                // Fall back to index.html for SPA routing
                file = new File(WEB_DIR + "/index.html");
            }
            String mime = getMime(path);
            byte[] bytes = Files.readAllBytes(file.toPath());
            ex.getResponseHeaders().set("Content-Type", mime);
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            ex.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = ex.getResponseBody()) { os.write(bytes); }
        }

        private String getMime(String path) {
            if (path.endsWith(".html")) return "text/html; charset=utf-8";
            if (path.endsWith(".css"))  return "text/css; charset=utf-8";
            if (path.endsWith(".js"))   return "application/javascript; charset=utf-8";
            if (path.endsWith(".json")) return "application/json";
            if (path.endsWith(".png"))  return "image/png";
            if (path.endsWith(".svg"))  return "image/svg+xml";
            return "application/octet-stream";
        }
    }
}
