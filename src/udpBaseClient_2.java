// Java program to illustrate Client side
// Implementation using DatagramSocket
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class udpBaseClient_2 {
    private final DatagramSocket socket;
    private InetAddress address;
    private final int port = 7500;  // Default broadcast port

    public udpBaseClient_2(String networkAddress) throws Exception {
        socket = new DatagramSocket();
        address = InetAddress.getByName(networkAddress); 
    }

    public void sendEquipmentID(int equipmentID) {
        try {
            String message = String.valueOf(equipmentID);
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
            socket.send(packet);
            System.out.println("Sent Equipment ID: " + equipmentID + " to " + address.getHostAddress() + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNetworkAddress(String newNetwork) {
        try {
            address = InetAddress.getByName(newNetwork);
            System.out.println("Network address changed to: " + newNetwork);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        socket.close();
    }
}



