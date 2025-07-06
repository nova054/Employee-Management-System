package src;

public class DBConnectionTest {
    public static void main(String[] args) {
        try {
            java.sql.Connection conn = DBConnection.getConnection();
            System.out.println("Database connection successful!");
            conn.close();
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 