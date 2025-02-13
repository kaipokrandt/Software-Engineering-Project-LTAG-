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
        
        String sql = "ALTER TABLE players ADD CONSTRAINT unique_id UNIQUE (id);";
        // Establish the connection
        //Connection connection = null;
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {


            statement.executeUpdate(sql);    
            //System.out.println("Connected to the PostgreSQL database successfully!");
            return connection;

            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        return null;

        
    }


    public void retreiveEntries(){
        // Database connection details

        // Establish the connection
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            // Execute a query
            ResultSet resultSet = statement.executeQuery("SELECT * FROM players");

            // Process the result set
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String codename = resultSet.getString("codename");
                System.out.println("ID: " + id + ", Codename: " + codename);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }
   


   //INSERT INTO players(id,codename) VALUES('42','bob');


    public void addplayer(String playerName, int ID){
        // Database connection details

        String sql = "INSERT INTO players(id, codename) VALUES('" + ID + "','" + playerName + "') ON CONFLICT (id) DO NOTHING;";

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

    public void removePlayer(int ID){
        // Database connection details

        String sql = "DELETE FROM players WHERE id = " + ID + ";";

        // Establish the connection
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            System.out.println("Connected to the PostgreSQL database successfully!");

            // Execute a query
            statement.executeUpdate(sql);
            System.out.println("Player removed successfully!");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }  
    
    public void editPlayer(String newName , int ID){
        // Database connection details

        String sql = "UPDATE players SET codename = '" + newName + "' WHERE id = " + ID + ";";

        // Establish the connection
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            System.out.println("Connected to the PostgreSQL database successfully!");

            // Execute a query
            statement.executeUpdate(sql);
            System.out.println("Player edited successfully!");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

    public void clearTable()
    {
        String sql = "TRUNCATE TABLE players;";

        // Establish the connection
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            System.out.println("Connected to the PostgreSQL database successfully!");

            // Execute a query
            statement.executeUpdate(sql);
            System.out.println("Table cleared successfully!");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

