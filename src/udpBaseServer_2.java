import java.io.IOException;
import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.net.BindException;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class udpBaseServer_2 {

    private static final int PORT = 7500;
    private static final int RECEIVE_PORT = 7501; // Default broadcast port
    private final byte[] buffer = new byte[256];

    private int redScore = 0;
    private int greenScore = 0;

    private List<String> redPlayers = new ArrayList<>();
    private List<String> greenPlayers = new ArrayList<>();

    private EntryScreen entryScreen = new EntryScreen(null, null);
    private database db;

    private int shooterID;
    private int team;

    private ScheduledExecutorService scheduler;
    private Set<Integer> baseHitPlayerIDs = new HashSet<>();

    public udpBaseServer_2(database db, EntryScreen entryScreen) {
        this.db = db;
        fetchPlayersFromDatabase();
        this.entryScreen = entryScreen;
        if (entryScreen != null) {
            startScheduler();
        }
    }

    public void setEntryScreen(EntryScreen entryScreen) {
        this.entryScreen = entryScreen;
    }

    public void createSocket() {
        DatagramSocket receiveSocket = null;
        DatagramSocket sendSocket = null;
    
        try {
            // Create a socket to receive messages on RECEIVE_PORT
            receiveSocket = new DatagramSocket(RECEIVE_PORT);
            System.out.println("UDP Server started... Listening on port " + RECEIVE_PORT);
    
            // Create a socket to send messages on PORT
            sendSocket = new DatagramSocket();
    
            while (true) {
                // Receive a packet
                DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
                receiveSocket.receive(receivedPacket);
    
                String receivedMessage = new String(receivedPacket.getData(), 0, receivedPacket.getLength());
                System.out.println("Received: " + receivedMessage);
    
                // Process the received message
                processMessage(receivedMessage);
    
                if (receivedMessage.equalsIgnoreCase("bye")) {
                    System.out.println("Client sent 'bye'... Server shutting down.");
                    break;
                }
    
                // Send acknowledgment back to the sender on PORT
                String reply = "ACK";
                byte[] replyBytes = reply.getBytes();
                DatagramPacket replyPacket = new DatagramPacket(
                        replyBytes, replyBytes.length,
                        receivedPacket.getAddress(), PORT // Send to the normal PORT
                );
                sendSocket.send(replyPacket);
            }
        } catch (BindException e) {
            System.err.println("ERROR: Port " + RECEIVE_PORT + " is already in use. Please close any running instances and try again.");
        } catch (IOException e) {
            System.err.println("IOException in UDP server: " + e.getMessage());
        } finally {
            if (receiveSocket != null && !receiveSocket.isClosed()) {
                receiveSocket.close();
            }
            if (sendSocket != null && !sendSocket.isClosed()) {
                sendSocket.close();
            }
        }
    }

    private void fetchPlayersFromDatabase() {
        try {
            ResultSet rs = db.retreiveEntries();
            while (rs.next()) {
                String team = rs.getString("team");
                String playerID = rs.getString("id");

                if ("Red".equalsIgnoreCase(team)) {
                    redPlayers.add(playerID);
                } else if ("Green".equalsIgnoreCase(team)) {
                    greenPlayers.add(playerID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void broadcastMessage(String message) {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] messageBytes = message.getBytes();
            DatagramPacket packet = new DatagramPacket(
                messageBytes, messageBytes.length, 
                InetAddress.getByName("127.0.0.1"), 7500 // Address and port of the traffic generator
            );
            socket.send(packet);
            System.out.println("Broadcasted: " + message);
        } catch (IOException e) {
            System.err.println("Error broadcasting message: " + e.getMessage());
        }
    }   

    public void sendCode(String code) {
        try {
            // Create a DatagramSocket
            DatagramSocket socket = new DatagramSocket();

            // Prepare the data to send
            String message = code;
            byte[] buffer = message.getBytes();

            // Specify the target address and port
            InetAddress address = InetAddress.getByName("127.0.0.1");
            int port = 7500;

            // Create a DatagramPacket to send the data
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);

            // Send the packet
            socket.send(packet);

            System.out.println("Message sent: " + message);

            // Close the socket
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    private void processMessage(String message) {
        switch (message) {
            case "202":
                System.out.println("Game Started!");
                startScheduler();
                broadcastMessage("202");
                break;

            case "221":
                System.out.println("Game Ended!");
                if (scheduler != null && !scheduler.isShutdown()) {
                    scheduler.shutdown();
                }
                break;

            case "43": 
                // These should only be processed with shooter ID context
                System.out.println("Invalid format for base hit (missing shooter ID)");
                break;
            case "53":
                // These should only be processed with shooter ID context
                System.out.println("Invalid format for base hit (missing shooter ID)");
                break;

            default:
                if (message.contains(":")) {
                    String[] parts = message.split(":");
                    if (parts.length == 2) {
                        try {
                            shooterID = Integer.parseInt(parts[0]);
                            int targetOrCode = Integer.parseInt(parts[1]);

                            String shooterTeam = db.getTeamByID(shooterID);

                            if (targetOrCode == 43) {
                                // Green base hit
                                if ("Red".equalsIgnoreCase(shooterTeam)) {
                                    baseHitPlayerIDs.add(shooterID);
                                    String shooterTag = getTaggedPlayer(shooterID);
                                    System.out.println(shooterTag + " hit the Green base! +100 Red");
                                    redScore += 100;
                                    if (entryScreen != null) {
                                        //entryScreen.markPlayerWithBaseHit(shooterID, "Green");
                                    entryScreen.appendPlayAction(shooterTag + " hit the Green base! (BASE)");
                                        stylelizedBaseHitRepaint(targetOrCode, shooterID);
                                    

                                }
                                    updateScores();
                                } else {
                                    System.out.println("Ignored: " + shooterID + " attempted to hit their own base (Green)");
                                }
                                return;
                            } else if (targetOrCode == 53) {
                                // Red base hit
                                if ("Green".equalsIgnoreCase(shooterTeam)) {
                                    baseHitPlayerIDs.add(shooterID);
                                    String shooterTag = getTaggedPlayer(shooterID);
                                    System.out.println(shooterTag + " hit the Red base! +100 Green");
                                    greenScore += 100;
                                    if (entryScreen != null) {
                                        //entryScreen.markPlayerWithBaseHit(shooterID, "Red");
                                    entryScreen.appendPlayAction(shooterTag + " hit the Red base! (BASE)");
                                        stylelizedBaseHitRepaint(targetOrCode, shooterID);


                                }
                                    updateScores();
                                } else {
                                    System.out.println("Ignored: " + shooterID + " attempted to hit their own base (Red)");
                                }
                                return;
                            }

                            // Standard player vs player hit
                            int targetID = targetOrCode;
                            String shooterTag = getTaggedPlayer(shooterID);
                            String targetTag = getTaggedPlayer(targetID);

                            System.out.println(shooterTag + " hit " + targetTag);

                            String targetTeam = db.getTeamByID(targetID);

                            if (shooterTeam != null && targetTeam != null) {
                                if (shooterTeam.equals(targetTeam)) {
                                    // Friendly fire
                                    if (shooterTeam.equalsIgnoreCase("Red")) {
                                        redScore -= 10;
                                    } else {
                                        greenScore -= 10;
                                    }
                                    System.out.println(shooterTag + " hit their own teammate " + targetTag);
                                } else {
                                    // Enemy hit
                                    if (shooterTeam.equalsIgnoreCase("Red")) {
                                        redScore += 10;
                                    } else {
                                        greenScore += 10;
                                    }
                                    System.out.println(shooterTag + " hit an enemy " + targetTag);
                                }

                                if (entryScreen != null) {
                                    entryScreen.appendPlayAction(shooterTag + " hit " + targetTag);
                                }

                                updateScores();
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("Invalid player ID format: " + message);
                        }
                    }
                } else {
                    System.out.println("Unknown command received: " + message);
                }
        }
    }

    private void updateScores() {
        if (entryScreen != null) {
            entryScreen.updateScores(redScore, greenScore);
        }
    }

    private String getTaggedPlayer(int playerID) {
        String name = db.getUserNameByID(playerID);
        boolean isBaseShooter = baseHitPlayerIDs.contains(playerID);
        return "Player (" + playerID + ":" + (name != null ? name : "Unknown") + (isBaseShooter ? " B" : "") + ")";
    }

    public void startScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        scheduler = Executors.newScheduledThreadPool(1);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable flashHigherScore = () -> {
            SwingUtilities.invokeLater(() -> {
                if (entryScreen == null) {
                    System.out.println("entryScreen is null!");
                } else {
                    entryScreen.flashHigherScore(redScore, greenScore);
                }
            });
        };

        scheduler.scheduleAtFixedRate(flashHigherScore, 0, 350, TimeUnit.MILLISECONDS);
    }

    public void stylelizedBaseHitRepaint(int targOpCode, int shooterId) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
        Runnable stylizedBaseHit = () -> {
    
            // Get the shooter's team and target the correct team panel
            String shooterTeam = db.getTeamByID(shooterId);
            final JPanel teamPanel;
    
            if ("red".equalsIgnoreCase(shooterTeam)) {
                teamPanel = entryScreen.redTeamPlayerPanel;
            } else {
                teamPanel = entryScreen.greenTeamPlayerPanel;
            }
    
            // Now use the correct team panel
            SwingUtilities.invokeLater(() -> {
                if (entryScreen == null) {
                    System.out.println("entryScreen is null!");
                } else {
                    entryScreen.updatePlayerPanel(teamPanel, Integer.toString(shooterId), shooterTeam);
                }
            });
        };
    
        scheduler.scheduleAtFixedRate(stylizedBaseHit, 0, 350, TimeUnit.MILLISECONDS);
    }
}

    

