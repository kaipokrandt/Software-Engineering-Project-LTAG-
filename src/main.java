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
        
        
        EntryScreen EntryScreen = new EntryScreen();
        //create entry screen
        EntryScreen.setDB(db);
        EntryScreen.createAndShowGUI();

        

        //db.addplayer("Bob",42);
        //db.addplayer("Bob",42);

        db.retreiveEntries();
    }


    
}
