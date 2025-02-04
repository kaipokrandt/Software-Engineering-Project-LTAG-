import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class database {
    private final String url = "jdbc:postgresql://localhost:5432/";
    private final String user = "yourusername";
    private final String password = "yourpassword";

    public void createDatabase() {
        String dbName = "newdatabase";
        String createDatabaseQuery = "CREATE DATABASE " + dbName;

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate(createDatabaseQuery);
                System.out.println("Database created successfully");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
