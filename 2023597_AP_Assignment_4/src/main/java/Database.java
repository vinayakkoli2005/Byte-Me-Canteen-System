import java.io.*;
import java.util.*;

public class Database {
    public static ArrayList<Customer> customers = new ArrayList<>();
    public static ArrayList<Customer> VIP = new ArrayList<>();
    public static ArrayList<Administrator> admins = new ArrayList<>();


    public static LinkedHashMap<String,ArrayList<Food>> Order = new LinkedHashMap<>();       // customer name and his food ordeer list
    public static LinkedHashMap<String,ArrayList<Food>> VIP_Order = new LinkedHashMap<>();       // customer name and his food order list
    public static ArrayList<Food> Cancelled_Order = new ArrayList<>();

    public static ArrayList<Food> Daily_sales = new ArrayList<>();

    public static HashMap<String, Integer> foodFrequency = new HashMap<>();

    public Database(){}

    public static Integer orderfreq=0;

    public static void saveData() {
        for (Map.Entry<String, ArrayList<Food>> entry : Order.entrySet()) {
            String customerName = entry.getKey();
            ArrayList<Food> orderList = entry.getValue();

            Customer customer=null;

            for(Customer c:Database.customers){
                if(c.getName().equals(customerName)){
                    customer=c;
                }
            }

            String fileName = customerName + "_order_history.txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (Food food : orderList) {
                    writer.write("NO"+" | "+food.getName() + " | " + food.getQuantity() + " | " + food.getPrice() + " | " + food.getStatus());
                    writer.newLine();
                }
                System.out.println("Order history saved for customer: " + customerName);
            } catch (IOException e) {
                System.out.println("Error saving order history for " + customerName + ": " + e.getMessage());
            }
        }

        for (Map.Entry<String, ArrayList<Food>> entry : VIP_Order.entrySet()) {
            String customerName = entry.getKey();
            ArrayList<Food> orderList = entry.getValue();


            String fileName = "VIP_" + customerName + "_order_history.txt";

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (Food food : orderList) {
                    writer.write("YES"+" | "+food.getName() + " | " + food.getQuantity() + " | " + food.getPrice()+ " | " + food.getStatus());
                    writer.newLine();
                }
                System.out.println("Order history saved for VIP customer: " + customerName);
            } catch (IOException e) {
                System.out.println("Error saving order history for VIP customer " + customerName + ": " + e.getMessage());
            }
        }
    }
    public static void loadOrderData() {
        // Load normal customer order history
        for (Customer customer : customers) {
            String fileName = customer.getName() + "_order_history.txt";
            ArrayList<Food> orderList = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split("\\|");
                    if (details.length == 5) {
                        String name = details[1].trim();
                        int quantity = Integer.parseInt(details[2].trim());
                        double price = Double.parseDouble(details[3].trim());
                        String status = details[4].trim();
                        Food food =new Food(name, quantity, true, status);
                        System.out.println(customer.getName()+ " "+name+"added");
                        orderList.add(food);
                        customer.Order_history.add(food);
                    }
                }
                Order.put(customer.getName(), orderList);

                System.out.println("Loaded order history for customer: " + customer.getName());
            } catch (FileNotFoundException e) {
                System.out.println("No order history found for customer: " + customer.getName());
            } catch (IOException e) {
                System.out.println("Error loading order history for " + customer.getName() + ": " + e.getMessage());
            }
        }

        for (Customer vip : VIP){
            String fileName = "VIP_" + vip.getName() + "_order_history.txt";
            ArrayList<Food> orderList = new ArrayList<>();

            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] details = line.split("\\|");
                    if (details.length == 5) {
                        String name = details[1].trim();
                        int quantity = Integer.parseInt(details[2].trim());
                        double price = Double.parseDouble(details[3].trim());
                        String status = details[4].trim();
                        Food food =new Food(name, quantity, true, status);
                        orderList.add(food);
                        System.out.println(food.getName()+" Added Successfully");
                        vip.Order_history.add(food);
                    }
                }

                VIP_Order.put(vip.getName(), orderList);

                System.out.println("Loaded order history for VIP customer: " + vip.getName());

            } catch (FileNotFoundException e) {
                System.out.println("No order history found for VIP customer: " + vip.getName());
            } catch (IOException e) {
                System.out.println("Error loading order history for VIP customer " + vip.getName() + ": " + e.getMessage());
            }
        }
    }


    public static void Save_User_Data() {
        String fileName = "Customer_Data.txt";

        for(Customer c:Database.customers){
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {

                writer.write(c.getName()+ " | "+ c.getRoll_number()+ " | " + "NO");
                writer.newLine();

                System.out.println(c.getName() +" Data saved successfully");
            } catch (IOException e) {
                System.out.println("Error saving Data for " + c.getName() + ": " + e.getMessage());
            }
        }
        for(Customer c:Database.VIP){
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName,true))) {

                writer.write(c.getName()+ " | "+ c.getRoll_number()+ " | " + "YES");
                writer.newLine();

                System.out.println("Order history saved for customer: " + c.getName());
            } catch (IOException e) {
                System.out.println("Error saving order history for " + c.getName() + ": " + e.getMessage());
            }
        }
    }
    public static void loadUserData() {
        String fileName = "Customer_Data.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split("\\|");
                if (details.length == 3) {
                    String name = details[0].trim();
                    String rollNumber = details[1].trim();
                    String isVIP = details[2].trim();
                    boolean isVIPCustomer =true ;
                    if(isVIP.equals("NO")){
                        isVIPCustomer = false;
                        customers.add(new Customer(name, Integer.parseInt(rollNumber) ,isVIPCustomer));
                    }
                    else{
                        VIP.add(new Customer(name, Integer.parseInt(rollNumber) ,isVIPCustomer));
                    }
                }
            }
            System.out.println("User data loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("No user data found. Starting fresh.");
        } catch (IOException e) {
            System.out.println("Error loading user data: " + e.getMessage());
        }
    }



}
