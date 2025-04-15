import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class udpBaseServer_2 {

    final int PORT = 7500;
    byte[] buffer = new byte[256];
    
    // Score variables and labels
    private int redScore = 0;
    private int greenScore = 0;
    private JLabel redScoreLabel;
    private JLabel greenScoreLabel;

    // Constructor that accepts labels
    public udpBaseServer_2(JLabel redScoreLabel, JLabel greenScoreLabel) {
        this.redScoreLabel = redScoreLabel;
        this.greenScoreLabel = greenScoreLabel;
    }

    public void createSocket() {
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            System.out.println("UDP Server started... Listening on port " + PORT);

            while (true) {
                DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivedPacket);

                String receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                System.out.println("Received: " + receivedMessage);
                processMessage(receivedMessage);

                if (receivedMessage.equalsIgnoreCase("bye")) {
                    System.out.println("Client sent 'bye'... Server shutting down.");
                    break;
                }

                // Echo response for python traffic generator to keep going
                String reply = "ACK";
                byte[] replyBytes = reply.getBytes();
                DatagramPacket replyPacket = new DatagramPacket(replyBytes, replyBytes.length,
                        receivedPacket.getAddress(), receivedPacket.getPort());
                socket.send(replyPacket);
            }

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(String message) {
        if (message.equals("202")) {
            System.out.println("Game Started!");
        } else if (message.equals("221")) {
            System.out.println("Game Ended!");
        } else if (message.equals("53")) {
            System.out.println("Red base scored! +100 points for Green Team.");
            greenScore += 100;
            SwingUtilities.invokeLater(() -> greenScoreLabel.setText("Green Score: " + greenScore));
        } else if (message.equals("43")) {
            System.out.println("Green base scored! +100 points for Red Team.");
            redScore += 100;
            SwingUtilities.invokeLater(() -> redScoreLabel.setText("Red Score: " + redScore));
        } else if (message.contains(":")) {
            String[] parts = message.split(":");
            if (parts.length == 2) {
                try {
                    int shooterID = Integer.parseInt(parts[0]);
                    int targetID = Integer.parseInt(parts[1]);
                    System.out.println("Player " + shooterID + " hit Player " + targetID);
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Invalid player ID format: " + message);
                }
            }
        } else {
            System.out.println("⚠️ Unknown command received: " + message);
        }
    }
}