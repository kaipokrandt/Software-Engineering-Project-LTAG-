//imports here


public class main{

    
    public static void main(String[] args) {
        //hi world
        System.out.println("Starting Photon...");

        SplashScreen SplashScreen = new SplashScreen();
        //create splash screen
        SplashScreen.showSplashScreen();

        EntryScreen EntryScreen = new EntryScreen();
        //create entry screen
        EntryScreen.showPlayerEntryScreen();

        database db = new database();
        db.retreiveEntries();
    }


    
}
