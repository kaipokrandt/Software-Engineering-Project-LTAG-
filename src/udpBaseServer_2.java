import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

public class udpBaseServer_2 {

    private static final int PORT = 7500;
    private final byte[] buffer = new byte[256];

    private int redScore = 0;
    private int greenScore = 0;

    private List<String> redPlayers = new ArrayList<>();
    private List<String> greenPlayers = new ArrayList<>();

    private EntryScreen entryScreen = new EntryScreen(null, null);
    private database db;

    public udpBaseServer_2(database db, EntryScreen entryScreen) {
        this.db = db;
        fetchPlayersFromDatabase();
        this.entryScreen = entryScreen;
        if(entryScreen != null) {
            startScheduler();
        }
        
    }

    public void setEntryScreen(EntryScreen entryScreen) {
        this.entryScreen = entryScreen;

        
    }
    

    public void createSocket() {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
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
                processMessage(receivedMessage);

                // Step 6: Exit the server if "bye" is received
                if (receivedMessage.equalsIgnoreCase("bye")) {
                    System.out.println("Client sent 'bye'... Server shutting down.");
                    break;
                }

                String reply = "ACK";
                byte[] replyBytes = reply.getBytes();
                DatagramPacket replyPacket = new DatagramPacket(
                        replyBytes, replyBytes.length,
                        receivedPacket.getAddress(), receivedPacket.getPort()
                );
                socket.send(replyPacket);
            }

        } catch (IOException e) {
            e.printStackTrace();
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

    private void processMessage(String message) {
        switch (message) {
            case "202":
                System.out.println("Game Started!");
                startScheduler();
                break;
    
            case "221":
                System.out.println("Game Ended!");
                break;
    
            case "43":
                System.out.println("Green base scored! +100 points for Red Team.");
                redScore += 100;
                updateScores();
                break;
    
            case "53":
                System.out.println("Red base scored! +100 points for Green Team.");
                greenScore += 100;
                updateScores();
                break;
    
            default:
                if (message.contains(":")) {
                    String[] parts = message.split(":");
                    if (parts.length == 2) {
                        try {
                            int shooterID = Integer.parseInt(parts[0]);
                            int targetOrCode = Integer.parseInt(parts[1]);
    
                            String shooterName = db.getUserNameByID(shooterID);
                            String shooterTag = "Player (" + shooterID + ":" + (shooterName != null ? shooterName : "Unknown") + ")";
    
                            if (targetOrCode == 43) {
                                // Red player hit the Green base
                                System.out.println(shooterTag + " hit the Green base! +100 Red");
                                redScore += 100;
                                if (entryScreen != null) {
                                    entryScreen.appendPlayAction(shooterTag + " hit the Green base! (BASE)");
                                }
                                updateScores();
                                return;
    
                            } else if (targetOrCode == 53) {
                                // Green player hit the Red base
                                System.out.println(shooterTag + " hit the Red base! +100 Green");
                                greenScore += 100;
                                if (entryScreen != null) {
                                    entryScreen.appendPlayAction(shooterTag + " hit the Red base! (BASE)");
                                }
                                updateScores();
                                return;
                            }
    
                            int targetID = targetOrCode;
                            String targetName = db.getUserNameByID(targetID);
                            String targetTag = "Player (" + targetID + ":" + (targetName != null ? targetName : "Unknown") + ")";
    
                            System.out.println(shooterTag + " hit " + targetTag);
    
                            String shooterTeam = db.getTeamByID(shooterID);
                            String targetTeam = db.getTeamByID(targetID);
    
                            if (shooterTeam != null && targetTeam != null) {
                                if (shooterTeam.equals(targetTeam)) {
                                    if (shooterTeam.equals("Red")) {
                                        redScore -= 10;
                                    } else if (shooterTeam.equals("Green")) {
                                        greenScore -= 10;
                                    }
                                    System.out.println(shooterTag + " hit their own teammate " + targetTag);
                                } else {
                                    if (shooterTeam.equals("Red")) {
                                        redScore += 10;
                                    } else if (shooterTeam.equals("Green")) {
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

    

    

    public void startScheduler() {
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



    
}
