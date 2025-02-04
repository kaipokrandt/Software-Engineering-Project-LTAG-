import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public Connection connect_to_db(){
        Connection conn = null;

        String url = "jdbc:mariadb://localhost:3306/players";
        String user = "root";
        String password = "student";
        
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

        }catch (Exception e){
            System.out.println(e);
        }
        

        return conn;
    }
}
