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


    public void retreiveEntries(){

    }
   


   //INSERT INTO players(id,codename) VALUES('42','bob');


    public void addplayer(String playerName, int ID){
        // Database connection details
        String url = "jdbc:postgresql://localhost:5432/photon";
        String user = "student";
        String password = "student";

        String sql = "INSERT INTO players(id, codename) VALUES('" + ID + "','" + playerName + "');";

        // Establish the connection
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            System.out.println("Connected to the PostgreSQL database successfully!");

            // Execute a query
            statement.executeUpdate(sql);
            System.out.println("Player added successfully!");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
}

