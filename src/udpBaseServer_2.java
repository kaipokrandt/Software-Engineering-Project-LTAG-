// Java program to illustrate Server side UDP communication
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class udpBaseServer_2 {
    public static void main(String[] args) {
        final int PORT = 7501; // Server listens on port 7501
        byte[] buffer = new byte[256]; // Buffer size for incoming packets

        try {
            // Step 1: Create a socket to listen on UDP port 7501
            DatagramSocket socket = new DatagramSocket(PORT);
            System.out.println("UDP Server started... Listening on port " + PORT);

            while (true) {
                // Step 2: Create a DatagramPacket to receive incoming data
                DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);

                // Step 3: Receive the data
                socket.receive(receivedPacket);

                // Step 4: Convert received bytes into a string
                String receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength());

                // Print the received data
                System.out.println("Received: " + receivedMessage);

                // Step 5: Process the received message
                processMessage(receivedMessage);

                // Step 6: Exit the server if "bye" is received
                if (receivedMessage.equalsIgnoreCase("bye")) {
                    System.out.println("Client sent 'bye'... Server shutting down.");
                    break;
                }
            }

            // Close the socket
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to process game messages based on the received data
    private static void processMessage(String message) {
        if (message.equals("202")) {
            System.out.println("Game Started!");
        } else if (message.equals("221")) {
            System.out.println("Game Ended!");
        } else if (message.equals("53")) {
            System.out.println(" Red base scored! +100 points for Green Team.");
        } else if (message.equals("43")) {
            System.out.println("Green base scored! +100 points for Red Team.");
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
            System.out.println("⚠️ Unknown command received!: " + message);
            System.out.println("Hey How Are you!");
        }
    }
}
