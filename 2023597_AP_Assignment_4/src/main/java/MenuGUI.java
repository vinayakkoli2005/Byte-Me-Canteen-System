import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MenuGUI {
    private JFrame frame;

    public MenuGUI(List<Food> snacks, List<Food> drinks, List<Food> meals) {

        MenuItem(snacks, drinks, meals);

        frame = new JFrame("Canteen Menu");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 400);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Snacks", createCategoryPanel(snacks));
        tabbedPane.addTab("Drinks", createCategoryPanel(drinks));
        tabbedPane.addTab("Meals", createCategoryPanel(meals));

        frame.add(tabbedPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }


    private JPanel createCategoryPanel(List<Food> categoryItems) {
        JPanel panel = new JPanel(new BorderLayout());
        String[] columns = {"Name", "Price", "Availability"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

        for (Food item : categoryItems) {
            String[] row = {item.getName(), "$" + item.getPrice(), item.isAvailable() ? "Yes" : "No"};
            tableModel.addRow(row);
        }

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }


    private void MenuItem(List<Food> snacks, List<Food> drinks, List<Food> meals) {
        ArrayList<Food> items = new ArrayList<Food>();
        for (Food i : snacks) {
            items.add(i);
        }
        for (Food i : drinks) {
            items.add(i);
        }
        for (Food i: meals) {
            items.add(i);
        }
        for(Food i: items) {
            System.out.println(i.getName() + " -> " + i.getStatus());
        }
    }

}
