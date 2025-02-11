import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class database {
    
    private static String url = "jdbc:postgresql://localhost:5432/photon";
    private static String user = "student";
    private static String password = "student";
    
    public static Connection connectToDatabase(){
        // Database connection details
        

        // Establish the connection
        //Connection connection = null;
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            //System.out.println("Connected to the PostgreSQL database successfully!");
            return connection;

            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return null;
        
    }

}

