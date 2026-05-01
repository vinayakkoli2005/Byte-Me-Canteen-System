import java.util.*;
public class Customer {
    private final String name;
    private final Integer Roll_number;
    private boolean isVIP =false;
    Boolean Refunded=false;


    ArrayList<Food> Cart= new ArrayList<>();   //food name, food

    ArrayList<Food> Order_history = new ArrayList<>();   //food name


    public Customer(String name, Integer Roll_number, boolean isVIP) {

        this.name = name;
        this.Roll_number = Roll_number;

        if (isVIP) {
            this.isVIP = true;
            Database.VIP.add(this);
        }
        else {
            this.isVIP = false;
            Database.customers.add(this);
        }

    }

    public String getName() {
        return name;
    }
    public Integer getRoll_number() {
        return Roll_number;
    }

    // ----------------------------------------------------------------------------------------   CART OPS--------

    public void addToCart(String name,Integer Quantity,String Specialreq) {
        if(Quantity<=0){
            return;
        }

        Boolean found = false;
        for (Food i : Menu.items) {
            if (i.getName().equalsIgnoreCase(name)) {
                if (i.isAvailable()) {

                    Iterator<Food> iterator = Cart.iterator();
                    while (iterator.hasNext()) {
                        Food f = iterator.next();
                        if (f.getName().equalsIgnoreCase(name)) {
                            iterator.remove();
                        }
                    }
                    i.setQuantity(Quantity);
                    i.setSpecialreq(Specialreq);
                    Cart.add(i);
                    found = true;
                    System.out.println("Item added to the cart.");
                    break;
                }
                if (!i.isAvailable()) {
                    System.out.println("Item is out of stock and cannot be added to the cart.");
                    return;
                }

            }
        }
        if (!found) {
            System.out.println("Item not found in the menu.");
        }
    }


    public void removeItem(String itemName) {
        boolean found = false;

        for (int i = 0; i < Cart.size(); i++) {
            Food foodItem = Cart.get(i);
            if (foodItem.getName().equalsIgnoreCase(itemName)) {
                Cart.remove(i);
                found = true;
                System.out.println("Item removed from the cart.");
                break;
            }
        }

        if (!found) {
            System.out.println("Item not found in the cart.");
        }
    }

    public double getTotalPrice() {
        double total = 0;
        for (Food item : Cart) {
            total +=      (item.getPrice())  *  (item.getQuantity())  ;
        }
        return total;
    }

    public void displayCart() {
        if (Cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        System.out.println("Items in your cart:");
        for (Food item : Cart) {
            System.out.println(item);
        }
        System.out.println("Total Price: $" + getTotalPrice());
    }

    public void checkout(String paymentDetails, String deliveryAddress) {
        if (Cart.isEmpty()) {
            System.out.println("Cannot checkout. Your cart is empty.");
            return;
        }
        System.out.println("Checkout Details:");
        displayCart();
        System.out.println("Payment Details: " + paymentDetails);
        System.out.println("Delivery Address: " + deliveryAddress);
        System.out.println("Order has been placed successfully.");
        Cart.clear();
    }

    public void setVIP(boolean b) {
        this.isVIP = b;
    }

    public boolean isVIP() {
        return isVIP;
    }
    public void setRefunded(boolean b) {
        this.Refunded = b;
    }
    public boolean isRefunded() {
        return Refunded;
    }

    //------------------------------------------------------------------------------------------------------------------------------

}