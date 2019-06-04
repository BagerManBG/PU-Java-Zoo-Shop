package zoo_shop.database;

import java.sql.*;

public class ConnectionManager {
    private static Connection con;

    public static Connection getConnection() {
        String url = "jdbc:mysql://localhost:3307/zoo_shop";
        String driverName = "com.mysql.jdbc.Driver";
        String username = "zoo-shop";
        String password = "admin";

        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(url, username, password);
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        } catch (ClassNotFoundException ex) {
            System.out.println(ex);
        }
        return con;
    }
}
