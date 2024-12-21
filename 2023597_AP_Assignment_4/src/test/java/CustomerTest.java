import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testAddItemToCart() {

        Food food1 = new Food("Pizza", 10.0, true,"Snacks");
        Menu.items = new ArrayList<>();
        Menu.items.add(food1);

        Customer customer = new Customer("John Doe", 12345, false);

        customer.addToCart("Pizza", 2, "Extra cheese");

        assertEquals(20.0, customer.getTotalPrice(), "Total price should be updated correctly.");
        assertEquals(1, customer.Cart.size(), "Cart size should be updated after adding an item.");
        assertEquals("Pizza", customer.Cart.get(0).getName(), "Added item should match the expected item.");

    }

    @Test
    void testModifyItemQuantity() {
        Food food1 = new Food("Burger", 5.0, true,"Snacks");
        Menu.items = new ArrayList<>();
        Menu.items.add(food1);

        Customer customer = new Customer("Jane Doe", 67890, false);
        customer.addToCart("Burger", 2, "No onions");

        customer.addToCart("Burger", 4, "No onions");

        assertEquals(20.0, customer.getTotalPrice(), "Total price should be updated correctly after modifying quantity.");
        assertEquals(1, customer.Cart.size(), "Cart should not duplicate items when quantity is modified.");
        assertEquals(4, customer.Cart.get(0).getQuantity(), "Item quantity should be updated correctly.");
    }

    @Test
    void testNegativeQuantity() {
        Food food1 = new Food("Pasta", 8.0, true,"Snacks");
        Menu.items = new ArrayList<>();
        Menu.items.add(food1);

        Customer customer = new Customer("Alice Smith", 11111, false);

        customer.addToCart("Pasta", -3, "No garlic"); // Try to add with negative quantity

        assertEquals(0.0, customer.getTotalPrice(), "Total price should not change if negative quantity is given.");
        assertTrue(customer.Cart.isEmpty(), "Cart should remain empty when attempting to add an item with negative quantity.");
    }

    @Test
    void testOutOfStockItem() {
        Food food1 = new Food("Fries", 3.0, false, "Snacks");
        Food food2 = new Food("Pizza", 10.0, true, "Snacks");
        Menu.items = new ArrayList<>();
        Menu.items.add(food1);
        Menu.items.add(food2);

        Customer customer = new Customer("Chris Evans", 55555, false);

        customer.addToCart("Fries", 1, "No salt"); // Attempt to add out-of-stock item

        assertEquals(0.0, customer.getTotalPrice(), "Total price should not change for out-of-stock item.");
        assertTrue(customer.Cart.isEmpty(), "Cart should remain empty when attempting to add an out-of-stock item.");
    }

}
