import java.util.*;


public class Menu {


    public static ArrayList<Food> items = new ArrayList<>(); // name,food            -----
    public static ArrayList<Food> Drinks = new ArrayList<>();//                          | ========  >>>         need to add explicitly not adding in constructor
    public static ArrayList<Food> Snacks = new ArrayList<>();//                          |
    public static ArrayList<Food> Meals = new ArrayList<>();//                       -----
    public static PriorityQueue<Double> Prices = new PriorityQueue<>();     // adding the price of the items in the food constructor so no need to add explicitly

    public void addItem(Food item) {
        items.add( item);
    }
    public void updateItem(String name, double newPrice, boolean isAvailable) {
    }





}
