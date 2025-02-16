import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class database {
    //if password authentication failed - 
    //in terminal "psql photon" then "ALTER USER student WITH PASSWORD 'student';"
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
            
            if(!e.getMessage().equals("ERROR: relation \"unique_id\" already exists") ){
                System.out.println(e.getMessage());
            }
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
            System.err.println("Error 2: " + e.getMessage());
        }

    }
   


   //INSERT INTO players(id,codename) VALUES('42','bob');

    /**
   * Adds the Player's Name and ID into the database.
   *
   * @param playerName Name of the player that is added to the database
   * @param ID ID of the player that is added to the databse
   * @return Returns Void.
   */
    public void addplayer(String playerName, int ID){
        // Database connection details

        String sql = "INSERT INTO players(id, codename) VALUES('" + ID + "','" + playerName + "') ON CONFLICT (id) DO NOTHING;";
        //String sql = "INSERT INTO players(id, codename) VALUES('" + ID + "','" + playerName + "');";

        // Establish the connection
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            System.out.println("Connected to the PostgreSQL database successfully!");

            // Execute a query
            statement.executeUpdate(sql);
            System.out.println("Player added successfully!");

        } catch (Exception e) {
            System.err.println("Error 3: " + e.getMessage());
        }

    }

    public void removePlayerbyId(int ID){

        //Delete players from the databse, accepts integer;

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
            System.err.println("Error 4: " + e.getMessage());
        }

    }  
    
    public void editPlayer(String newName , int ID){ 
        
        //EDIT TABLE players SET codename = 'newName' WHERE id = 1;
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
            System.err.println("Error 5: " + e.getMessage());
        }

    }

    /**
     
        Clears the table in the database.

        @return Void
     */


    
    public void clearTable(){

        // Clears the table.

        String sql = "TRUNCATE TABLE players;";

        // Establish the connection
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            System.out.println("Connected to the PostgreSQL database successfully!");

            // Execute a query
            statement.executeUpdate(sql);
            System.out.println("Table cleared successfully!");

        } catch (Exception e) {
            System.err.println("Error 6: " + e.getMessage());
        }
    }

    /**
     * Compares against the table in the datase to see if the Id exists.
     * @param Id Id that needs to be checked against the database.
     * @return True: If the Id exists in the database. False if it does not.
     */

    public boolean checkIfIdExists(int Id){

        //Checks if ID exists in the database, returns true if it does, false if it does not.

        String sql = "SELECT COUNT(*) FROM players WHERE id = " + Id + ";";
        boolean IDexists = false;

        // Establish the connection
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            System.out.println("Connected to the PostgreSQL database successfully!");

            // Execute a query
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                if(Id == resultSet.getInt(1)){
                    IDexists = true;
                }
            }
            System.out.println("ID checked successfully!");

        } catch (Exception e) {
            System.err.println("Error 7: " + e.getMessage());
        }
        return IDexists;
    }
}

