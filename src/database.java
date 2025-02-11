import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class database {
    
    public void createDatabase(){
        // Database connection details
        String url = "jdbc:postgresql://localhost:5432/photon";
        String user = "student";
        String password = "student";

        // Establish the connection
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            System.out.println("Connected to the PostgreSQL database successfully!");

            // Execute a query
            //String sql = "SELECT id, name FROM your_table_name";
            //ResultSet resultSet = statement.executeQuery(sql);

            // Process the result set
            // while (resultSet.next()) {
            //     int id = resultSet.getInt("id");
            //     String name = resultSet.getString("name");
            //     System.out.println("ID: " + id + ", Name: " + name);
            // }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

