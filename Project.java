 
import java.sql.*;
import java.util.Scanner;

class InventoryManagementSystem {

    private final String url = "jdbc:mysql://localhost:3306/java";
    private final String user = "root";  // MySQL username
    private final String password = "rohitdemo@123";  // MySQL password

    // Connect to the MySQL database
    public Connection connect() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish the connection
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.getMessage());
        }
        return null;  // Return null if connection fails
    }
    public void testConnection() {
        try (Connection conn = connect()) {
            if (conn != null) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Failed to establish connection.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    

    // Add a product to the database
    public void addProduct(String productId, String productName, int stockLevel, double price) {
        String query = "INSERT INTO products (product_id, product_name, stock_level, price) VALUES (?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = connect();
            if (conn == null) {
                throw new SQLException("Failed to establish connection. Connection is null.");
            }
    
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, productId);
            pstmt.setString(2, productName);
            pstmt.setInt(3, stockLevel);
            pstmt.setDouble(4, price);
            pstmt.executeUpdate();
            System.out.println("Product added successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.close();  // Close the connection after use
                } catch (SQLException e) {
                    System.out.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    // Update product stock level
public void updateProductStock(String productId, int newStockLevel) {
    String query = "UPDATE products SET stock_level = ? WHERE product_id = ?";
    try (Connection conn = connect();
         PreparedStatement pstmt = conn.prepareStatement(query)) {

        pstmt.setInt(1, newStockLevel);
        pstmt.setString(2, productId);
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("Stock updated successfully.");
        } else {
            System.out.println("Product not found.");
        }

    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}


    // Remove product from database
    public void removeProduct(String productId) {
        String query = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, productId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Product removed successfully.");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Add a supplier to the database
    public void addSupplier(String supplierId, String supplierName) {
        String query = "INSERT INTO suppliers (supplier_id, supplier_name) VALUES (?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, supplierId);
            pstmt.setString(2, supplierName);
            pstmt.executeUpdate();
            System.out.println("Supplier added successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Generate stock report
    public void generateStockReport() {
        String query = "SELECT * FROM products";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Stock Report:");
            while (rs.next()) {
                String productId = rs.getString("product_id");
                String productName = rs.getString("product_name");
                int stockLevel = rs.getInt("stock_level");
                double price = rs.getDouble("price");
                System.out.printf("Product ID: %s, Name: %s, Stock: %d, Price: $%.2f%n",
                        productId, productName, stockLevel, price);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

class Project {
    public static void main(String[] args) {
        InventoryManagementSystem ims = new InventoryManagementSystem();

        ims.testConnection();
        
        Scanner scanner = new Scanner(System.in);
        
        boolean running = true;
        while (running) {
            System.out.println("\nInventory Management System");
            System.out.println("1. Add Product");
            System.out.println("2. Update Product Stock");
            System.out.println("3. Remove Product");
            System.out.println("4. Add Supplier");
            System.out.println("5. Generate Stock Report");
            System.out.println("6. Exit");
            System.out.print("Select an option: ");
            int choice = Integer.parseInt(scanner.nextLine());  // Use nextLine to avoid skipping input

            switch (choice) {
                case 1:
                    System.out.print("Enter product ID: ");
                    String productId = scanner.nextLine();
                    System.out.print("Enter product name: ");
                    String productName = scanner.nextLine();
                    System.out.print("Enter stock level: ");
                    int stockLevel = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter price: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    ims.addProduct(productId, productName, stockLevel, price);
                    break;
                case 2:
                    System.out.print("Enter product ID: ");
                    productId = scanner.nextLine();
                    System.out.print("Enter new stock level: ");
                    stockLevel = scanner.nextInt();
                    ims.updateProductStock(productId, stockLevel);
                    break;
                
                case 3:
                    System.out.print("Enter product ID: ");
                    productId = scanner.nextLine();
                    ims.removeProduct(productId);
                    break;
                case 4:
                    System.out.print("Enter supplier ID: ");
                    String supplierId = scanner.nextLine();
                    System.out.print("Enter supplier name: ");
                    String supplierName = scanner.nextLine();
                    ims.addSupplier(supplierId, supplierName);
                    break;
                case 5:
                    ims.generateStockReport();
                    break;
                case 6:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        }
        scanner.close();
    }
}