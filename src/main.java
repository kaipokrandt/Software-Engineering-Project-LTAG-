
public class main {

    public static void main(String[] args) {
        System.out.println("Starting Photon...");



        // Show splash screen
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.showSplashScreen();

        // Connect to database
        database db = new database();
        db.connectToDatabase();

        // Create and start the UDP server! (store reference)
        

        // Create UDP client
        udpBaseClient_2 udpClient = null;
        try {
            udpClient = new udpBaseClient_2("127.0.0.1");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create EntryScreen and link it to the server
        EntryScreen entryScreen = new EntryScreen(db, udpClient);

        udpBaseServer_2 udpServer = new udpBaseServer_2(db, entryScreen);
        new Thread(() -> udpServer.createSocket()).start();

        // Wait briefly for server to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Show the GUI
        entryScreen.createAndShowGUI();
    }
}
