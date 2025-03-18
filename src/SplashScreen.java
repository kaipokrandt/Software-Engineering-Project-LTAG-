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
        
        // Get screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        
        // Create new window
        JWindow splash = new JWindow();

        try {
            // Find logo.jpg and read it
            File imageFile = new File("../images/logo.jpg");

            if (!imageFile.exists()) {
                imageFile = new File("./images/logo.jpg");
            }
            if (imageFile.exists()) {
                BufferedImage img = ImageIO.read(imageFile);
            
                // Get the image's original width and height
                int imgWidth = img.getWidth();
                int imgHeight = img.getHeight();

                // Calculate the scaling factor to fit the image to the screen
                double scaleX = (double) screenWidth / imgWidth;
                double scaleY = (double) screenHeight / imgHeight;
                double scaleFactor = Math.min(scaleX, scaleY); // Maintain aspect ratio

                // Calculate the new dimensions
                int newWidth = (int) (imgWidth * scaleFactor);
                int newHeight = (int) (imgHeight * scaleFactor);

                // Create a new image scaled to the desired size
                BufferedImage scaledImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = scaledImg.createGraphics();
                g2d.drawImage(img, 0, 0, newWidth, newHeight, null);
                g2d.dispose();

                // Display the scaled image
                JLabel label = new JLabel(new ImageIcon(scaledImg));
                splash.getContentPane().add(label, BorderLayout.CENTER);
            
                // Set window size to match the scaled image dimensions
                splash.setSize(newWidth, newHeight);
            } else {
                System.out.println("Splash image not found.");
            }

        } catch (IOException e) {
            System.out.println("Error loading splash image: " + e.getMessage());
        }

        // Center the splash screen
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        // Display splash for 3 seconds
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
        }

        // Remove splash and dispose
        splash.setVisible(false);
        splash.dispose();
    } 
}