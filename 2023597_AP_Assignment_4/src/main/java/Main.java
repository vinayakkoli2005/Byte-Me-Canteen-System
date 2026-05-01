import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.ActionListener;
import java.util.*;

public class Main extends JFrame  {
    static void addItem() {
        Food item = new Food("Pizza", 10.99, true, "Snacks");
        Menu.items.add(item);
        Menu.Snacks.add(item);
        Food item1 = new Food("Burger", 8.99, true, "Snacks");
        Menu.items.add(item1);
        Menu.Snacks.add(item1);
        Food item2 = new Food("Small Meal", 12.99, true, "Meals");
        Menu.items.add(item2);
        Menu.Meals.add(item2);
        Food item3 = new Food("Coke", 2.99, true, "Drinks");
        Menu.items.add(item3);
        Menu.Drinks.add(item3);
        Food item4 = new Food("Coffee", 3.99, true, "Drinks");
        Menu.items.add(item4);
        Menu.Drinks.add(item4);
        Food item5 = new Food("Regular Meal", 5.99, true, "Meals");
        Menu.items.add(item5);
        Menu.Meals.add(item5);
        Food item6 = new Food("Large Meal", 4.99, true, "Meals");
        Menu.items.add(item6);
        
        Menu.Meals.add(item6);
        Food item7 = new Food("Jumbo Meal", 3.99, true, "Meals");
        Menu.items.add(item7);
        Menu.Meals.add(item7);
        Food item8 = new Food("Punjabi Meal", 6.99, true, "Meals");
        Menu.items.add(item8);
        Menu.Meals.add(item8);
    }
    static void addCustomer() {
        Customer customer = new Customer("John", 12345,false);
        Customer customer1 = new Customer("Jane", 12346,true);
        Customer customer2 = new Customer("Jack", 12347,true);
        Customer customer3 = new Customer("Jill", 12348,false);
        Customer customer4 = new Customer("Joe", 12349,false);
        Customer customer5 = new Customer("Jenny", 12350,false);
        Customer customer6 = new Customer("Jenny", 12351,true);
        Customer customer7 = new Customer("Jenny", 12352,true);
        Customer customer8 = new Customer("Jenny", 12353,true);
        Customer customer9 = new Customer("Jenny", 12354,false);
    }
    public static void main(String[] args) {
        addItem();
        addCustomer();



        Scanner sc = new Scanner(System.in);

        System.out.println("\nWelcome to Byte Me! Your College Canteen Companion");

        Database.loadUserData();
        Database.loadOrderData();


        // Main loop for role selection
        while (true) {
            System.out.println("\nPlease select your role: ");
            System.out.println("1. Customer");
            System.out.println("2. Admin");
            System.out.println("3. Exit");

            int role = sc.nextInt();
            sc.nextLine();

            if (role == 1) {

                Customer customer = null;

                System.out.println("\nWelcome Customer!");

                System.out.println("Please enter your name: ");
                String Name = sc.nextLine();

                for(Customer c : Database.customers){
                    if(c.getName().equalsIgnoreCase(Name)){
                        customer =c;
                        System.out.println("Welcome customer");
                    }
                }

                for(Customer c : Database.VIP){
                    if(c.getName().equalsIgnoreCase(Name)){
                        customer =c;
                        System.out.println("Welcome VIP customer");
                        break;
                    }
                }
                if( customer == null){
                    System.out.println("Please enter your roll number: ");
                    Integer Roll_number = sc.nextInt();
                    sc.nextLine();

                    System.out.println("Do you want to become a VIP customer by paying 5$ monthly subscription ? ( yes / no )");
                    String VIP_choice = sc.nextLine();
                    boolean VIP = false;
                    if(VIP_choice.equalsIgnoreCase("yes")){
                        VIP = true;
                    }else{
                        VIP = false;
                    }

                    customer= new Customer(Name,Roll_number,VIP);
                }

                while (true) {
                    System.out.println("1. Browse Menu");
                    System.out.println("2. Cart Operations");
                    System.out.println("3. Order Tracking");
                    System.out.println("4. Items Review");
                    System.out.println("5. Back to Role Selection");
                    System.out.println("6. GUI Implementation");


                    int customerOption = sc.nextInt();
                    sc.nextLine();

                    if (customerOption == 1) {

                        Boolean exit = false;
                        while( !exit ) {

                            System.out.println("\nBrowse Menu:");
                            System.out.println("1. View All Items");
                            System.out.println("2. Search Items");
                            System.out.println("3. Filter by Category");
                            System.out.println("4. Sort by Price");
                            System.out.println("5 Exit");

                            int browseOption = sc.nextInt();
                            sc.nextLine();

                            if (browseOption == 1) {
                                System.out.println("Displaying all items...");
                                for (Food i : Menu.items) {
                                    System.out.println(i.getName() + ": $" + i.getPrice());
                                }
                            } else if (browseOption == 2) {
                                System.out.println("Enter item name to search:");
                                String searchItem = sc.nextLine();
                                boolean found = false;
                                for (Food i : Menu.items) {

                                    if (i.getName().equalsIgnoreCase(searchItem)) {
                                        System.out.println(i.getName() + ": $" + i.getPrice());
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    System.out.println("Item not found.");
                                }

                            } else if (browseOption == 3) {
                                System.out.println("Filtering by category...");
                                System.out.println("\n1. Snacks");
                                for(Food i: Menu.Snacks) {
                                    System.out.println(i.getName() + ": $" + i.getPrice());
                                }
                                System.out.println("\n2. Drinks");
                                for(Food i: Menu.Drinks) {
                                    System.out.println(i.getName() + ": $" + i.getPrice());
                                }
                                System.out.println("\n3. Meals");
                                for(Food i: Menu.Meals) {
                                    System.out.println(i.getName() + ": $" + i.getPrice());
                                }
                            } else if (browseOption == 4) {
                                System.out.println("Sorting by low to high price...");
                                for(int i=0;i<Menu.Prices.size();i++){
                                    Double price=Menu.Prices.poll();
                                    for(Food j: Menu.items) {
                                        if(j.getPrice() ==price ) {
                                            System.out.println(j.getName() + ": $" + j.getPrice());
                                        }
                                    }
                                }


                            } else if (browseOption==5) {
                                exit = true;


                            } else {
                                System.out.println("Invalid option. Try again.");
                            }
                            System.out.println("\n");
                        }

                    } else if (customerOption == 2) {

                        Boolean exit = false;
                        while( !exit ) {
                            System.out.println("\nCart Operations:");
                            System.out.println("1. Add Items to Cart");
                            System.out.println("2. Remove Items from Cart");
                            System.out.println("3. View Total");
                            System.out.println("4. Checkout");
                            System.out.println("5. Exit");
                            int cartOption = sc.nextInt();
                            sc.nextLine();

                            if (cartOption == 1) {
                                System.out.println("Enter item name to add to cart:");

                                String itemName = sc.nextLine();
                                System.out.println("Enter quantity:");
                                int quantity = sc.nextInt();
                                sc.nextLine();
                                System.out.println("Any Special request yes/no");
                                String reqchoice = sc.nextLine();
                                String req=null;
                                if((reqchoice.equals("yes"))){
                                    System.out.println("Enter the Special request");
                                    req = sc.nextLine();
                                    customer.addToCart(itemName, quantity, req);
                                }
                                else{
                                    customer.addToCart(itemName, quantity,req);
                                }



                            } else if (cartOption == 2) {
                                System.out.println("Enter Item name to remove from cart:");
                                String itemName = sc.nextLine();
                                customer.removeItem(itemName);
                            } else if (cartOption == 3) {
                                System.out.println(customer.getTotalPrice());
                            } else if (cartOption == 4) {
                                System.out.println("Enter the delivery Address");
                                String address = sc.nextLine();
                                System.out.println("Select mode of payment");
                                System.out.println("1. Cash on Delivery");
                                System.out.println("2. Online Payment");
                                int paymentOption = sc.nextInt();
                                sc.nextLine();
                                if (paymentOption == 1) {
                                    System.out.println("Order placed successfully!");
                                } else if (paymentOption == 2) {
                                    System.out.println("Enter the UPI ID");
                                    String upiId = sc.nextLine();
                                    System.out.println("Order placed successfully!");
                                } else {
                                    System.out.println("Invalid option. Try again.");
                                    exit=true;
                                }
                                for(Food i:customer.Cart){
                                    i.setCustomer_name(customer.getName());
                                    i.setDelivery_address(address);
                                }
                                if (customer.isVIP()) {
                                    if (Database.VIP_Order.containsKey(customer.getName())) {
                                        ArrayList<Food> existingOrder = Database.VIP_Order.get(customer.getName());
                                        for( Food i : existingOrder){
                                            if(Database.foodFrequency.containsKey(i.getName())){
                                                Integer freq =Database.foodFrequency.get(i.getName());
                                                freq+=i.getQuantity();
                                                Database.foodFrequency.put(i.getName(), freq);

                                            }else{
                                                Database.foodFrequency.put(i.getName(), i.getQuantity());
                                            }

                                        }
                                        Database.orderfreq++;
                                        existingOrder.addAll(customer.Cart);
                                        System.out.println("VIP");
                                    } else {
                                        for( Food i : customer.Cart){
                                            if(Database.foodFrequency.containsKey(i.getName())){
                                                Integer freq =Database.foodFrequency.get(i.getName());
                                                freq+=i.getQuantity();
                                                Database.foodFrequency.put(i.getName(), freq);

                                            }else{
                                                Database.foodFrequency.put(i.getName(), i.getQuantity());
                                            }

                                        }
                                        Database.orderfreq++;
                                        Database.VIP_Order.put(customer.getName(), new ArrayList<>(customer.Cart));
                                        System.out.println("VIP");
                                    }
                                } else {
                                    // Place order in regular Order map for non-VIP customers
                                    if (Database.Order.containsKey(customer.getName())) {
                                        ArrayList<Food> existingOrder = Database.Order.get(customer.getName());

                                        for( Food i : existingOrder){
                                            if(Database.foodFrequency.containsKey(i.getName())){
                                                Integer freq =Database.foodFrequency.get(i.getName());
                                                freq+=i.getQuantity();
                                                Database.foodFrequency.put(i.getName(), freq);

                                            }else{
                                                Database.foodFrequency.put(i.getName(), i.getQuantity());
                                            }

                                        }
                                        Database.orderfreq++;
                                        existingOrder.addAll(customer.Cart);
                                    } else {
                                        for( Food i : customer.Cart){
                                            if(Database.foodFrequency.containsKey(i.getName())){
                                                Integer freq =Database.foodFrequency.get(i.getName());
                                                freq+=i.getQuantity();
                                                Database.foodFrequency.put(i.getName(), freq);

                                            }else{
                                                Database.foodFrequency.put(i.getName(), i.getQuantity());
                                            }

                                        }
                                        Database.orderfreq++;
                                        Database.Order.put(customer.getName(), new ArrayList<>(customer.Cart));
                                    }
                                }

                                customer.Order_history.addAll(customer.Cart);
                                customer.Cart.clear();

                            } else if (cartOption == 5) {
                                exit = true;
                            } else {
                                System.out.println("Invalid option. Try again.");
                            }
                        }

                    } else if (customerOption == 3) {
                        // Order Tracking options
                        System.out.println("\nOrder Tracking:");
                        System.out.println("1. View Order Status");
                        System.out.println("2. Cancel Order");
                        System.out.println("3. View Order History");
                        int trackOption = sc.nextInt();
                        sc.nextLine();

                        if (trackOption == 1) {
                            if( (!customer.isVIP()) && (Database.Order.containsKey(customer.getName()))   ){
                                ArrayList<Food> food = Database.Order.get(customer.getName());

                                for(Food i:food){
                                    System.out.println(i.getName()+" -> "+i.getStatus());
                                }

                            } else if ((customer.isVIP()) && ( Database.VIP_Order.containsKey(customer.getName()))){
                                ArrayList<Food> food = Database.VIP_Order.get(customer.getName());
                                for(Food i:food){
                                    System.out.println(i.getName()+" -> "+i.getStatus());
                                }

                            } else{
                                System.out.println("No order found");
                            }
                        } else if (trackOption == 2) {
                            if((!customer.isVIP()) && Database.Order.containsKey(customer.getName())){
                                Database.Cancelled_Order.addAll(Database.Order.get(customer.getName()));
                                Database.Order.remove(customer.getName());
                                System.out.println("Order cancelled successfully! ");


                            } else if ((customer.isVIP()) && Database.VIP_Order.containsKey(customer.getName())) {

                                Database.Cancelled_Order.addAll(Database.VIP_Order.get(customer.getName()));
                                Database.VIP_Order.remove(customer.getName());
                                System.out.println("Order cancelled successfully! ");

                            } else{
                                System.out.println("No order found");
                            }

                        } else if (trackOption == 3) {

                            if(!customer.Order_history.isEmpty()){

                                for(Food i:customer.Order_history){
                                    System.out.println(i.getName());
                                }

                            } else{
                                System.out.println("No order found");

                            }
                        } else {
                            System.out.println("Invalid option. Try again.");
                        }

                    }else if (customerOption == 4) {
                        System.out.println("1. Provide review");
                        System.out.println("2. View Reviews");
                        System.out.println("3. Exit");
                        int reviewOption = sc.nextInt();
                        sc.nextLine();
                        if(reviewOption==1){
                            System.out.println("Enter the name if the FoodItem for reviewing");
                            String name = sc.nextLine();
                            boolean found = false;

                            for(Food i : customer.Order_history){
                                if(i.getName().equalsIgnoreCase(name)){
                                    System.out.println("Enter the Review for the food item");
                                    String review = sc.nextLine();
                                    i.Reviews.put(customer.getName(),review);
                                    System.out.println("Review added successfully");
                                    found = true;
                                    break;
                                }
                            }
                            if(!found){
                                System.out.println("You have not ordered this food item so cannot give review on this");
                            }
                        }else if(reviewOption==2){
                            System.out.println("Enter the name of the FoodItem to view reviews:");
                            String name = sc.nextLine();

                            boolean found = false;

                            for (Food item : Menu.items) {
                                if (item.getName().equalsIgnoreCase(name)) {
                                    System.out.println("Reviews for " + name + ":");
                                    for (HashMap.Entry<String, String> entry : item.getReviews().entrySet()) {
                                        System.out.println(entry.getKey() + " -> " + entry.getValue());
                                    }
                                    found = true;
                                    break;
                                }
                            }

                            if (!found) {
                                System.out.println("Item not found in menu.");
                            }

                        }else if(reviewOption==3){
                            break;
                        }


                    } else if (customerOption == 5) {
                        break; // Go back to role selection
                    } else if (customerOption == 6) {

                        new MenuGUI(Menu.Snacks, Menu.Drinks, Menu.Meals);
                        break;

                    } else {
                        System.out.println("Invalid input. Please try again.");
                    }
                }

            } else if (role == 2) {
                // Admin Interface loop
                System.out.println("Enter your name");
                String admin_name = sc.nextLine();
                Administrator admin = new Administrator(admin_name);
                while (true) {



                    System.out.println("\nWelcome Admin!");
                    System.out.println("1. Menu Management");
                    System.out.println("2. Order Management");
                    System.out.println("3. Report Generation");
                    System.out.println("4. Change day");
                    System.out.println("5. Back to Role Selection");
                    System.out.println("6. GUI Implementation");

                    int adminOption = sc.nextInt();
                    sc.nextLine();

                    if (adminOption == 1) {
                        // Menu Management options
                        System.out.println("\nMenu Management:");
                        System.out.println("1. Add New Item");
                        System.out.println("2. Update Existing Item");
                        System.out.println("3. Remove Item");
                        System.out.println("4. Modify Prices");
                        System.out.println("5. Update Availability");
                        int menuOption = sc.nextInt();
                        sc.nextLine();

                        if (menuOption == 1) {
                            System.out.println("Enter the name of the new item:");
                            String name = sc.nextLine();
                            System.out.println("Enter the price of the new item:");
                            double price = sc.nextDouble();
                            sc.nextLine();
                            System.out.println("Enter the availability of the new item (true/false):");
                            boolean availability = sc.nextBoolean();
                            sc.nextLine();
                            System.out.println("Enter the category of the new item:");
                            String category = sc.nextLine();
                            Food item = new Food(name, price, availability, category);
                            boolean isAdded = false;
                            if(category.equalsIgnoreCase("Drinks")){
                                Menu.items.add(item);
                                Menu.Drinks.add(item);
                                isAdded = true;
                                System.out.println("Item added successfully");

                            }else if(category.equalsIgnoreCase("Snacks")){
                                Menu.items.add(item);
                                Menu.Snacks.add(item);
                                isAdded = true;
                                System.out.println("Item added successfully");

                            }else if(category.equalsIgnoreCase("Meals")){
                                Menu.items.add(item);
                                Menu.Meals.add(item);
                                isAdded = true;
                                System.out.println("Item added successfully");

                            }
                            if(!isAdded){
                                System.out.println("Item not added");
                            }


                        } else if (menuOption == 2) {
                            System.out.println("Enter the name of the item to update:");
                            String name = sc.nextLine();
                            boolean found = false;
                            for (Food item : Menu.items) {
                                if (item.getName().equalsIgnoreCase(name)) {
                                    System.out.println("Enter the new price of the item:");
                                    double price = sc.nextDouble();
                                    sc.nextLine();
                                    System.out.println("Enter the new availability of the item (true/false):");
                                    boolean availability = sc.nextBoolean();
                                    sc.nextLine();
                                    System.out.println("Enter the new category of the item:");
                                    String category = sc.nextLine();
                                    item.setPrice(price);
                                    item.setAvailability(availability);
                                    item.setCategory(category);
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                System.out.println("Item not found in menu.");
                            }



                        } else if (menuOption == 3) {

                            System.out.println("Enter the name of the item to remove:");
                            String name = sc.nextLine();

                            Food itemToRemove = null;
                            boolean found = false;

                            // Find the item in Menu.items without using an iterator
                            for (Food item : Menu.items) {
                                if (item.getName().equalsIgnoreCase(name)) {
                                    itemToRemove = item;
                                    found = true;
                                    break;
                                }
                            }

                            if (found && itemToRemove != null) {
                                String category = itemToRemove.getCategory();

                                // Remove from Menu.items
                                Menu.items.remove(itemToRemove);

                                // Remove from specific category list
                                if (category.equalsIgnoreCase("Drinks")) {
                                    Menu.Drinks.remove(itemToRemove);
                                } else if (category.equalsIgnoreCase("Snacks")) {
                                    Menu.Snacks.remove(itemToRemove);
                                } else if (category.equalsIgnoreCase("Meals")) {
                                    Menu.Meals.remove(itemToRemove);
                                } else {
                                    System.out.println("Unknown category: " + category);
                                }

                                System.out.println("Item '" + name + "' removed from menu and " + category + " category.");
                            } else {
                                System.out.println("Item not found in menu.");
                            }

                        } else if (menuOption == 4) {
                            System.out.println("Enter the name of the item to modify prices:");
                            String name = sc.nextLine();
                            boolean found = false;
                            for (Food item : Menu.items) {
                                if (item.getName().equalsIgnoreCase(name)) {
                                    found = true;
                                    System.out.println("Enter the new price of the item:");
                                    double price = sc.nextDouble();
                                    sc.nextLine();
                                    item.setPrice(price);
                                    System.out.println("Item price updated successfully.");;

                                    break;
                                    }
                                if (!found) {
                                System.out.println("Item not found in menu.");
                                }
                            }

                        } else if (menuOption == 5) {
                            System.out.println("Enter the name of the item to update availability:");
                            String name = sc.nextLine();
                            boolean found = false;
                            for (Food item : Menu.items) {
                                if (item.getName().equalsIgnoreCase(name)) {
                                    System.out.println("Enter the new availability of the item (true/false):");
                                    boolean availability = sc.nextBoolean();
                                    sc.nextLine();
                                    item.setAvailability(availability);
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                System.out.println("Item not found in menu.");
                            }
                        } else {
                            System.out.println("Invalid option. Try again.");
                        }

                    } else if (adminOption == 2) {
                        // Order Management options
                        System.out.println("\nOrder Management:");
                        System.out.println("1. View Pending Orders and update their status");
                        System.out.println("2. Process Refunds");

                        int orderMgmtOption = sc.nextInt();
                        sc.nextLine();

                        if (orderMgmtOption == 1) {
                            admin.View_Update_Pending_Orders();

                        } else if (orderMgmtOption == 2) {

                            admin.Process_Refund();

                        } else {
                            System.out.println("Invalid option. Try again.");
                        }


                    } else if (adminOption == 3) {
                        // Report Generation
                        System.out.println("\nGenerating Daily Sales Report...");

                        Double daily_sales = 0.0;
                        for(Food i : Database.Daily_sales){
                            daily_sales+=i.getPrice() * i.getQuantity();
                        }
                        System.out.println("Daily Sales: " + daily_sales);

                        // most populAR item
                        String mostFrequentItem = null;
                        int maxFrequency = 0;

                        // Iterate over each entry in foodFrequency to find the item with the highest frequency
                        for (Map.Entry<String, Integer> i : Database.foodFrequency.entrySet()) {
                            if (i.getValue() > maxFrequency) {
                                mostFrequentItem = i.getKey();
                                maxFrequency = i.getValue();
                            }
                        }

                        System.out.println("Most Popular Item: " + mostFrequentItem);

                        // total orders
                        System.out.println("Total Orders: " + Database.orderfreq);

                    }else if (adminOption == 4) {
                        Database.Daily_sales.clear();
                    }
                    else if (adminOption == 5) {
                        break;
                    } else if (adminOption == 6) {
                        System.out.println("Launching GUI for pending orders...");
                        new Order_History_GUI(Database.VIP_Order, Database.Order);
                        break;
                        
                    } else {
                        System.out.println("Invalid input. Please try again.");
                    }
                }

            } else if (role == 3) {
                System.out.println("Thank you for using Byte Me! Have a great day!");
                Database.saveData();
                Database.Save_User_Data();
                break;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }

        sc.close();

    }
}
