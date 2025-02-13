import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JWindow;

class SplashScreen {
   public static void showSplashScreen() {

        //create new window
        JWindow splash = new JWindow();

        try {
            //find logo.jpg and read it
            File imageFile = new File("../images/logo.jpg");
            BufferedImage img = ImageIO.read(imageFile);
            //

            //scale for testing purposes
            Image scaledImg = img.getScaledInstance(600,337,Image.SCALE_SMOOTH);

            //load
            JLabel label = new JLabel(new ImageIcon(scaledImg));
            splash.getContentPane().add(label, BorderLayout.CENTER);
        } catch (IOException e){
            System.out.println("Error loading splash image: " + e.getMessage());
        }

        //set image size and show it
        splash.setSize(600,337);
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        //display splash for 5 seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //remove splash and dispose
        splash.setVisible(false);
        splash.dispose();
    } 

}

