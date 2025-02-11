import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class database {
    
    private String url = "jdbc:postgresql://localhost:5432/photon";
    private String user = "student";
    private String password = "student";
    
    public Connection connectToDatabase(){
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

    public void createTables(){

        String createRedTeamTable = "CREATE TABLE IF NOT EXISTS red_team (" +
                                    "id SERIAL PRIMARY KEY, " +
                                    "name VARCHAR(50), " +
                                    "score INT)";
        
        String createGreenTeamTable = "CREATE TABLE IF NOT EXISTS green_team (" +
                                    "id SERIAL PRIMARY KEY, " +
                                    "name VARCHAR(50), " +
                                    "score INT)";
        
        try(Connection connection = connectToDatabase()) {

        } catch(Exception e){
            System.err.println("Error: " + e.getMessage());
        }


    }
}

