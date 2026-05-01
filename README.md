# Byte Me! — College Canteen Food Ordering System

A full-stack food ordering system built for a college canteen. Students can browse the menu, place orders, and track delivery status. Canteen staff get a complete admin panel to manage items, process orders, and generate reports.

---

## Live Demo

> Hosted on Netlify · Backend powered by Supabase
>
> **Admin credentials:** `admin` / `byteme123`

---

## Features

### Customer
- Browse menu with search, category filter, and sort by price
- Add items to cart with quantity and special requests
- VIP customers get priority order processing over regular customers
- Real-time order status tracking (Preparing → Ready → Delivered)
- Full order history
- Leave and view item reviews

### Admin Panel
- Password-protected login screen
- Add, update, and remove menu items
- View and update order statuses
- Removing an item auto-denies any pending orders containing it
- Daily sales report: total revenue, popular items, order count
- Customer management

### CLI + GUI (original assignment)
- Command-line interface for both customer and admin workflows
- Swing/AWT GUI for read-only menu and order display
- GUI reflects live CLI state via shared file I/O

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 11 |
| GUI | Java AWT / Swing |
| Web Frontend | Vanilla HTML + CSS + JS (single file) |
| Database | Supabase (PostgreSQL via REST API) |
| Hosting | Netlify (static) |
| Collections | ArrayList, TreeMap, PriorityQueue, TreeSet |
| File Handling | Java I/O Streams |
| Testing | JUnit 5 |

---

## Database Schema

Four tables in Supabase:

```
menu_items   — id, name, price, category, available
customers    — id, name, roll_number, is_vip
orders       — id, customer_name, is_vip, status, delivery_address, payment_method
order_items  — id, order_id, item_name, price, quantity, special_request
```

Run `web/schema.sql` in the Supabase SQL Editor to set up the database.

---

## Running Locally

### Web app (recommended)
Just open `web/index.html` in any browser — no server needed.

### Java CLI
```powershell
# Compile
javac -d target/classes src/main/java/*.java

# Run
java -cp target/classes Main
```

Or use the PowerShell script:
```powershell
.\run-web.ps1
```

---

## Project Structure

```
Byte me/
├── src/main/java/
│   ├── Main.java          # CLI entry point + menu seeding
│   ├── WebLauncher.java   # Web server entry point
│   ├── WebServer.java     # Embedded HTTP server (REST API)
│   ├── Food.java          # Menu item model
│   ├── Customer.java      # Customer model + cart logic
│   ├── Administrator.java # Admin operations
│   ├── Database.java      # File-based persistence
│   ├── Menu.java          # Menu state
│   └── Order.java         # Order model
├── web/
│   ├── index.html         # Full SPA (no framework)
│   └── schema.sql         # Supabase database setup
├── pom.xml
└── run-web.ps1
```

---

## Testing

```powershell
mvn test
```

JUnit 5 test cases cover:
- Ordering out-of-stock items
- Cart price calculation on add/modify
- Prevention of negative quantities

---

## Deployment

1. Create a free account at [supabase.com](https://supabase.com)
2. Run `web/schema.sql` in the SQL Editor
3. Paste your Project URL and anon key into `web/index.html`
4. Drag the `web/` folder to [netlify.com/drop](https://netlify.com/drop)

---

*Developed as a college assignment — IIIT Delhi*
