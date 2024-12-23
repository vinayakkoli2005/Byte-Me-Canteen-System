import java.util.*;
public class Food {


    String Delivery_address;
    String Customer_name;

    public static Integer popularfreq=0;

    String Specialreq;

    Integer Quantity;
    private final String name;
    private double price;
    private boolean available;
    private String category;
    private String status;

    HashMap<String,String> Reviews = new HashMap<>();  //customer name, and his review

    public Food(String name, double price, boolean available, String category) {
        this.name = name;
        this.price = price;
        this.available = available;
        this.category = category;
        this.status="Preparing";
        this.Quantity=0;
        if (category.equalsIgnoreCase("Drinks")) {
            this.category = "Drinks";

        } else if (category.equalsIgnoreCase("Snacks")) {
            this.category = "Snacks";

        } else if (category.equalsIgnoreCase("Meal")) {
            this.category = "Meal";
        }
        Menu.Prices.add((Double) price);
    }
    public String getName() {
        return name;
    }
    public Integer getQuantity() {
        return Quantity;
    }
    public void setQuantity(Integer Quantity) {
        this.Quantity = Quantity;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }
    public HashMap<String, String> getReviews() {
        return Reviews;
    }

    public String getCategory() {
        return category;
    }
    public String getStatus() {
        return status;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public void setAvailability(boolean availability) {
        this.available = availability;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public void setDelivery_address(String Delivery_address) {
        this.Delivery_address = Delivery_address;
    }
    public void setCustomer_name(String Customer_name) {
        this.Customer_name = Customer_name;
    }
    public String getDelivery_address() {
        return Delivery_address;
    }
    public String getCustomer_name() {
        return Customer_name;
    }
    public void setSpecialreq(String Specialreq) {
        this.Specialreq = Specialreq;
    }
    public String getSpecialreq() {
        return Specialreq;
    }
    @Override
    public String toString() {
        return Quantity+" -> "+name +" (Special request = "+ Specialreq+" )" ;
    }
}
