package zoo_shop.database;

import java.sql.*;

public class ConnectionManager {
    private static Connection con;

    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3306/zoo_shop";
        String driverName = "com.mysql.jdbc.Driver";
        String username = "root";
        String password = "";

        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                System.out.println("Failed to create the database connection.");
            }
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver not found.");
        }
        return con;
    }
}
