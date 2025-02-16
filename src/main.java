//imports here


public class main{

    
    public static void main(String[] args) {
        //hi world
        System.out.println("Starting Photon...");

        SplashScreen SplashScreen = new SplashScreen();
        //create splash screen
        SplashScreen.showSplashScreen();

        
        database db = new database();
        db.connectToDatabase();
        
        
        // Create the UDP client with a default IP (e.g., 127.0.0.1)
        udpBaseClient_2 udpClient = null;
        try {
            udpClient = new udpBaseClient_2("127.0.0.1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        EntryScreen EntryScreen = new EntryScreen();
        //create entry screen
        EntryScreen.setDB(db);
        EntryScreen.setUdpClient(udpClient);
        EntryScreen.createAndShowGUI();

        
        //db.checkIfIdExists(1);
        
        
    }
}
