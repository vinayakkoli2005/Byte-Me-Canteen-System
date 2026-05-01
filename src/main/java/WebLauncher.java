/**
 * Standalone web-only launcher.
 * Starts the HTTP server and seeds menu data without the blocking CLI.
 */
public class WebLauncher {
    public static void main(String[] args) throws Exception {
        // Seed menu items
        Main.addItem();
        // Load persisted customers if file exists
        Database.loadUserData();
        Database.loadOrderData();

        int port = 8080;
        if (args.length > 0) {
            try { port = Integer.parseInt(args[0]); } catch (NumberFormatException ignored) {}
        }

        WebServer ws = new WebServer(port);
        ws.start();

        System.out.println("========================================");
        System.out.println("  Byte Me! is running!");
        System.out.println("  Open: http://localhost:" + port);
        System.out.println("  Press Ctrl+C to stop.");
        System.out.println("========================================");

        // Keep alive
        Thread.currentThread().join();
    }
}
