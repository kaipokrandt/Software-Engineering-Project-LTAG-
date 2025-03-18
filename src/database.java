import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class database {
    //if password authentication failed - 
    //in terminal "psql photon" then "ALTER USER student WITH PASSWORD 'student';"
    private static String url = "jdbc:postgresql://localhost:5432/photon";
    private static String user = "student";
    private static String password = "student";
    
    public Connection connectToDatabase(){
        // Database connection details
        

        String sqlConstraint = "ALTER TABLE players ADD CONSTRAINT unique_id UNIQUE (id);";
        String sqlColumn = "ALTER TABLE players ADD COLUMN IF NOT EXISTS hardwareId INT;";
        String sqlColumnTeam = "ALTER TABLE players ADD COLUMN IF NOT EXISTS team VARCHAR(255);";
        // Establish the connection
        //Connection connection = null;
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            
            statement.executeUpdate(sqlConstraint);
            statement.executeUpdate(sqlColumn);
            statement.executeUpdate(sqlColumnTeam);
            //System.out.println("Connected to the PostgreSQL database successfully!");
            return connection;

            
        } catch (Exception e) {
            
            if(!e.getMessage().equals("ERROR: relation \"unique_id\" already exists") ){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }


    public ResultSet retreiveEntries() {
        try {
            // Establish the connection
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
    
            // Execute the query and return the result
            return statement.executeQuery("SELECT * FROM players");
        } catch (Exception e) {
            System.err.println("Error 2: " + e.getMessage());
            return null;
        }
    }
   
    public boolean checkIfIdExists(int Id) {
        String sql = "SELECT COUNT(*) FROM players WHERE id = " + Id + ";";
        boolean IDexists = false;

        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                if (Id == resultSet.getInt(1)) {
                    IDexists = true;
                }
            }
            System.out.println("ID checked successfully!");
        } catch (Exception e) {
            System.err.println("Error 7: " + e.getMessage());
        }
        return IDexists;
    }


   //INSERT INTO players(id,codename,hardwareId) VALUES('42','bob','4');

    /**
   * Adds the Player's Name and ID into the database.
   *
   * @param playerName Name of the player that is added to the database
   * @param ID ID of the player that is added to the databse
   * @param hardwareId Hardware ID of the player that is added to the database
   * @param team Team of the player that is added to the database
   * @return Returns Void.
   */
    public void addplayer(String playerName, int ID, int hardwareId, String team) {
        // Database connection details
        String sqlColumnTeam = "ALTER TABLE players ADD COLUMN IF NOT EXISTS team VARCHAR(255);";
        String sqlColumn = "ALTER TABLE players ADD COLUMN IF NOT EXISTS hardwareId INT;"; 
        String sql = "INSERT INTO players(id, codename, hardwareId, team) VALUES('" + ID + "','" + playerName + "', '" + hardwareId + "', '" + team + "') ON CONFLICT (id) DO UPDATE SET " + 
        "team = EXCLUDED.team, hardwareId = EXCLUDED.hardwareID;";
        //String sql = "INSERT OR REpINTO players(id, codename) VALUES('" + ID + "','" + playerName + "');";

        // Establish the connection
        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            System.out.println("Connected to the PostgreSQL database successfully!");

            // Execute a query
            statement.executeUpdate(sqlColumnTeam);
            statement.executeUpdate(sqlColumn);
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

    //edit players name/id method
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
            System.out.println("Table fully cleared successfully!");

        } catch (Exception e) {
            System.err.println("Error 6: " + e.getMessage());
        }
    }
    /**
     * Retrieves the username (codename) for a given player ID.
     *
     * @param playerID The ID of the player whose username is to be fetched.
     * @return The username (codename) if found, or null if the ID does not exist.
     */
    public String getUserNameByID(int playerID) {
        String codename = null;

        String sql = "SELECT codename FROM players WHERE id = " + playerID + ";";

        try (Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement()) {

            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                codename = resultSet.getString("codename");
            }
        } catch (Exception e) {
            System.err.println("Error retrieving username for ID " + playerID + ": " + e.getMessage());
        }

        return codename;
    }
}
