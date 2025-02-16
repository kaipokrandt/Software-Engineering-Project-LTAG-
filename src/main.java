//imports here


public class main{

    
    public static void main(String[] args) {
        //hi world
        System.out.println("Starting Photon...");

        //create and start udp server on different thread, port 7500
        new Thread(() -> {
            udpBaseServer_2 udpServer = new udpBaseServer_2();
            udpServer.createSocket();
        }).start();

        //wait for server to start
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        SplashScreen SplashScreen = new SplashScreen();
        //create splash screen
        SplashScreen.showSplashScreen();

        
        database db = new database();
        db.connectToDatabase();
        
        
        // Create the UDP client with a default IP (ex., 127.0.0.1)
        udpBaseClient_2 udpClient = null;
        try {
            udpClient = new udpBaseClient_2("127.0.0.1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        EntryScreen EntryScreen = new EntryScreen(db, udpClient);
        //create entry screen, pass db and udp client
        EntryScreen.setDB(db);
        EntryScreen.setUdpClient(udpClient);
        EntryScreen.createAndShowGUI();

    }
}
