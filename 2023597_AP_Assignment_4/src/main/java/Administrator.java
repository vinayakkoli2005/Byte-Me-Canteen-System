import java.util.*;
public class Administrator {

    private final String name;


    public Administrator(String name) {
        this.name = name;
    }

    public void View_Update_Pending_Orders() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Pending VIP Orders:");
        for (Map.Entry<String, ArrayList<Food>> entry : Database.VIP_Order.entrySet()) {
            String customerName = entry.getKey();
            ArrayList<Food> foodItems = entry.getValue();

            System.out.println("Customer: " + customerName);
            System.out.println("Order:");

            if (foodItems.isEmpty()) {
                System.out.println("  No items ordered.");
            } else {
                for (Food item : foodItems) {
                    System.out.println("  - " + item);
                }
            }
        }

        System.out.println("Pending Regular orders");
        for (Map.Entry<String, ArrayList<Food>> entry : Database.Order.entrySet()) {
            String customerName = entry.getKey();
            ArrayList<Food> foodItems = entry.getValue();

            System.out.println("Customer: " + customerName);
            System.out.println("Order:");

            if (foodItems.isEmpty()) {
                System.out.println("  No items ordered.");
            } else {
                for (Food item : foodItems) {
                    System.out.println("  - " + item);
                }
            }
        }

        if (!Database.VIP_Order.isEmpty()) {
            System.out.println("Processing VIP Orders:");
            System.out.print("Enter the number of VIP customers' orders to process: ");
            int vipCustomersToProcess = sc.nextInt();
            int processedVipCustomers = 0;

            // Iterate over VIP orders
            for (Map.Entry<String, ArrayList<Food>> entry : new ArrayList<>(Database.VIP_Order.entrySet())) {
                if (processedVipCustomers >= vipCustomersToProcess) break;

                String customerName = entry.getKey();
                ArrayList<Food> foodItems = entry.getValue();

                System.out.println("Customer: " + customerName);
                System.out.println("Order:");

                if (foodItems.isEmpty()) {
                    System.out.println("  No items ordered.");
                } else {
                    for (Food item : foodItems) {
                        item.setStatus("Out for Delivery");
                        Database.Daily_sales.add(item);
                        System.out.println("  - " + item);
                    }
                    processedVipCustomers++;
                    Database.VIP_Order.remove(customerName); // Remove processed order
                }
            }

            // Check if all VIP orders are processed
            if (!Database.VIP_Order.isEmpty()) {
                System.out.println("All VIP orders must be processed before regular orders.");
                return; // Exit if there are still pending VIP orders
            }
        }

        // Process Regular Orders if no VIP orders are left
        System.out.println("Processing Regular Orders:");
        System.out.print("Enter the number of regular customers' orders to process: ");
        int regularCustomersToProcess = sc.nextInt();
        int processedRegularCustomers = 0;

        // Iterate over regular orders
        for (Map.Entry<String, ArrayList<Food>> entry : new ArrayList<>(Database.Order.entrySet())) {
            if (processedRegularCustomers >= regularCustomersToProcess) break;

            String customerName = entry.getKey();
            ArrayList<Food> foodItems = entry.getValue();

            System.out.println("Customer: " + customerName);
            System.out.println("Order:");

            if (foodItems.isEmpty()) {
                System.out.println("  No items ordered.");
            } else {
                for (Food item : foodItems) {
                    item.setStatus("Out for Delivery"); // Mark as out for delivery
                    Database.Daily_sales.add(item);
                    System.out.println("  - " + item);
                }
                processedRegularCustomers++;
                Database.Order.remove(customerName); // Remove processed order
            }
        }

    }

    public void Process_Refund(){
        System.out.println("Cancelled order: ");
        for(Food i:Database.Cancelled_Order){
            String customerName = i.getCustomer_name();
            for(Customer j:Database.customers){
                if(j.getName().equalsIgnoreCase(customerName)){
                    j.setRefunded(true);
                    System.out.println("Customer: " + j.getName());
                    System.out.println("Order:");
                    System.out.println("  - " + i+" Refunded");
                }
            }

        }


    }

}
