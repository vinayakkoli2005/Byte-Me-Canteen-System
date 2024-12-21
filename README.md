Byte Me! Food Ordering System
-------------------------------


-> Overview
Byte Me! is a command-line and GUI-based food ordering system designed specifically for a college canteen. It allows students to browse the menu, place orders, track their delivery, and view order histories. It also provides canteen staff with features to manage the menu, process orders, and generate reports efficiently. The system uses advanced object-oriented programming concepts, Java collections, file handling, and testing frameworks to deliver a comprehensive solution.

---

-> Features

1. Command-Line Interface (CLI)
-> Admin Interface:
- Menu Management:
  - Add, update, and remove menu items.
  - Removed items automatically update pending orders containing the item to 'denied'.
- Order Management:
  - View, update, and prioritize orders (VIP > Regular).
  - Process refunds and handle special requests.
- Report Generation:
  - Generate daily sales reports with details like total sales, popular items, and total orders.

-> Customer Interface:
- Customer Types:
  - VIP Customers: Orders prioritized; status obtained by payment.
  - Regular Customers: Orders processed in order of receipt after VIP orders.
- Menu Browsing:
  - View menu with item details, search, filter by category, and sort by price.
- Cart Operations:
  - Add, modify, remove items, and view total before checkout.
- Order Tracking:
  - Real-time status updates, order history, and cancellation options.
- Item Reviews:
  - Leave and view reviews for menu items.

---

2. Graphical User Interface (GUI)
- GUI displays menu items and pending orders without modifying data.
- Design Features:
  - Intuitive navigation using buttons.
  - Tables to display menu items and order details.
- Integration:
  - Seamless data exchange between CLI and GUI using I/O stream management.
  - GUI reflects updates performed in CLI.

---

3. File Handling
Implemented two features:
- Order History:
  - Save and retrieve order histories for every user.
- Temporary Cart Storage:
  - Real-time updates of cart data stored in a temporary file during a session.

---

4. JUnit Testing
Developed test cases for:
- Ordering Out-of-Stock Items:
  - Ensures system prevents orders for unavailable items and displays error messages.
- Cart Operations:
  - Validates accurate price updates for adding/modifying items and prevents negative quantities.
                    
  ---

-> Technology Stack
- Programming Language: Java
- GUI Framework: AWT/Swing
- Collections: ArrayList, TreeMap, PriorityQueue, TreeSet
- File Handling: Java I/O Streams
- Testing: JUnit Framework

  
---

## Acknowledgements
This project was developed as part of a college assignment.                                         
                    
  
