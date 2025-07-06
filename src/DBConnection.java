package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/xe"; 
    private static final String USER = "ajay";
    private static final String PASS = "root"; 

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver not found.", e);
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
} 