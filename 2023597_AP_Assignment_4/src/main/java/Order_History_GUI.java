import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class Order_History_GUI {
    private JFrame frame;

    public Order_History_GUI(Map<String, ArrayList<Food>> vipOrders, Map<String, ArrayList<Food>> regularOrders) {



        frame = new JFrame("Pending Orders");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 500);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("VIP Orders", createOrdersPanel(vipOrders));
        tabbedPane.addTab("Regular Orders", createOrdersPanel(regularOrders));

        frame.add(tabbedPane, BorderLayout.CENTER);

        frame.setVisible(true);

        orderHistory(vipOrders, regularOrders);
    }

    private JPanel createOrdersPanel(Map<String, ArrayList<Food>> orders) {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"Customer Name", "Food Items"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Map.Entry<String, ArrayList<Food>> entry : orders.entrySet()) {
            String customerName = entry.getKey();
            ArrayList<Food> foodItems = entry.getValue();

            StringBuilder foodList = new StringBuilder();
            if (foodItems.isEmpty()) {
                foodList.append("No items ordered.");
            } else {
                for (Food item : foodItems) {
                    foodList.append(item.getName()).append(", ");
                }
                if (foodList.length() > 2) {
                    foodList.setLength(foodList.length() - 2);
                }
            }

            tableModel.addRow(new Object[]{customerName, foodList.toString()});
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void orderHistory(Map<String, ArrayList<Food>> vipOrders, Map<String, ArrayList<Food>> regularOrders) {
        System.out.println("Order History:");

        System.out.println("VIP Orders:");
        for (Map.Entry<String, ArrayList<Food>> entry : vipOrders.entrySet()) {
            String customerName = entry.getKey();
            System.out.print(customerName + ": ");
            for (Food food : entry.getValue()) {
                System.out.print(food.getName() + ", ");
            }
            System.out.println();
        }

        System.out.println("Regular Orders:");
        for (Map.Entry<String, ArrayList<Food>> entry : regularOrders.entrySet()) {
            String customerName = entry.getKey();
            System.out.print(customerName + ": ");
            for (Food food : entry.getValue()) {
                System.out.print(food.getName() + ", ");
            }
            System.out.println();
        }
    }
}
