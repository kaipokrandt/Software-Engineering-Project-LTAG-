import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class EntryScreen {

    //creates two different 2d arrays to read data from both teams
    private JTextField[][] redTeamFields;
    private JTextField[][] greenTeamFields;

    
    //implement database and udp client functionality to connnect
    public database db = new database();
    private udpBaseClient_2 udpClient;

    /**
     * 
     * @param db Databse that receives entries from the Entry Screen
     * @param udpClient 
     */

    public EntryScreen(database db, udpBaseClient_2 udpClient){
        this.db = db;
    }
    public void setDB(database db){
        this.db = db;
    }
    //sets udp client
    public void setUdpClient(udpBaseClient_2 udpClient){
        this.udpClient = udpClient;
    }

    //creates base for user GUI
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Entry Terminal");
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Get screen size dynamically
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.8);  // 80% of screen width
        int height = (int) (screenSize.height * 0.8); // 80% of screen height
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        JPanel redTeamPanel = createTeamPanel("RED TEAM", Color.RED, true);
        JPanel greenTeamPanel = createTeamPanel("GREEN TEAM", Color.GREEN, false);

        mainPanel.add(redTeamPanel);
        mainPanel.add(greenTeamPanel);

        JPanel bottomPanel = createBottomPanel();

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    //create team entry panel with player entry fields
    private JPanel createTeamPanel(String teamName, Color color, boolean isRedTeam) {
        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new BorderLayout());
        teamPanel.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel(teamName, SwingConstants.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(color);
        titleLabel.setForeground(Color.WHITE);
        teamPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel entryPanel = new JPanel(new GridLayout(16, 2, 5, 5));
        entryPanel.setBackground(Color.BLACK);

        // Add header row for ID and Name labels
        JLabel entryHeader = new JLabel("Player Number", SwingConstants.CENTER);
        entryHeader.setForeground(Color.WHITE);  // Ensure the header text is white
        JLabel idHeader = new JLabel("Enter ID", SwingConstants.CENTER);
        idHeader.setForeground(Color.WHITE);  // Ensure the header text is white
        JLabel nameHeader = new JLabel("Enter Name", SwingConstants.CENTER);
        nameHeader.setForeground(Color.WHITE);  // Ensure the header text is white

        entryPanel.add(entryHeader); // Player entry number column header
        entryPanel.add(idHeader);    // ID column header
        entryPanel.add(nameHeader);  // Name column header

        JTextField[][] playerFields = new JTextField[15][2];

        //create text fields for player entry
        //create text fields for player entry
        for (int i = 0; i < 15; i++) {
            JLabel label = new JLabel(String.valueOf(i + 1).trim(), SwingConstants.CENTER);
            label.setForeground(Color.WHITE);
            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            entryPanel.add(label);
            entryPanel.add(idField);
            entryPanel.add(nameField);
            playerFields[i][0] = idField;
            playerFields[i][1] = nameField;
        }

        if (isRedTeam)
        {
            redTeamFields = playerFields;
        }
        else
        {
            greenTeamFields = playerFields;
        }

        teamPanel.add(entryPanel, BorderLayout.CENTER);
        return teamPanel;
    }

    //create bottom buttons with keybind functionality through helper
    public JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 9, 5, 5));
        bottomPanel.setBackground(Color.BLACK);

        // Add buttons using the helper method.
        // ***only IP and Submit buttons currently have use***
        bottomPanel.add(createButton("Edit Game (F1)", KeyEvent.VK_F1, e -> {
            System.out.println("Edit Game button clicked");
            editGame();
        }));

        bottomPanel.add(createButton("Game Parameters (F2)", KeyEvent.VK_F2, e -> {
            System.out.println("Game Parameters button clicked");
            gameParameters();
        }));

        bottomPanel.add(createButton("Start Game (F3)", KeyEvent.VK_F3, e -> {
            System.out.println("Start Game button clicked");
            startGame();
        }));

        bottomPanel.add(createButton("PreEntered Games (F5)", KeyEvent.VK_F5, e -> {
            System.out.println("PreEntered Game button clicked");
            preEnteredGames();
        }));

        bottomPanel.add(createButton("View Game (F7)", KeyEvent.VK_F7, e -> {
            System.out.println("View Game button clicked");
            viewGame();
        }));

        bottomPanel.add(createButton("Flick Sync (F10)", KeyEvent.VK_F10, e -> {
            System.out.println("Flick Sync button clicked");
            flickSync();
        }));

        bottomPanel.add(createButton("Change IP (F11)", KeyEvent.VK_F11, e -> {
            System.out.println("Change IP button clicked");
            changeIPAddress();
        }));

        bottomPanel.add(createButton("Clear Game (F12)", KeyEvent.VK_F12, e -> {
            System.out.println("Clear Game button clicked");
            clearGame();
        }));

        bottomPanel.add(createButton("Submit", -1, e -> {
            System.out.println("Submit player button clicked");
            savePlayersToDatabase();
        }));

        return bottomPanel;
    }

    /**
    * Helper method to create a JButton with common properties.
    *
    * @param text           The button text.
    * @param mnemonic       The mnemonic key; pass -1 if no mnemonic is desired.
    * @param actionListener The ActionListener for the button.
    * @return A configured JButton.
    */
    private JButton createButton(String text, int mnemonic, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setForeground(Color.GREEN);
        button.setBackground(Color.DARK_GRAY);
        button.setPreferredSize(new Dimension(100, 50));
        if (mnemonic != -1) {
            button.setMnemonic(mnemonic);
        }
        button.addActionListener(actionListener);
        return button;
    }

    private void savePlayersToDatabase() {

        //error check to see if at least one player is entered in R/G team
        boolean redTeamHasPlayer = false;
        boolean greenTeamHasPlayer = false;

        for (int i = 0; i < 19; i++) {
            String redPlayerName = redTeamFields[i][1].getText().trim();
            String greenPlayerName = greenTeamFields[i][1].getText().trim();
            
            if (!redPlayerName.isEmpty()) {
                redTeamHasPlayer = true;
            }
            if (!greenPlayerName.isEmpty()) {
                greenTeamHasPlayer = true;
            }
        }

        if (!redTeamHasPlayer || !greenTeamHasPlayer) {
            JOptionPane.showMessageDialog(null, "At least one player must be entered for each team.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<String> codeNames = new ArrayList<String>();
        ArrayList<Integer> playerIds = new ArrayList<Integer>();
        
        //save the players from the red team
        for (int i = 0; i < 19; i++) {
            try {
                String playerName = redTeamFields[i][1].getText().trim();
                String idText = redTeamFields[i][0].getText().trim();
                
                if (!playerName.isEmpty() && !idText.isEmpty()) {
                    int playerID = Integer.parseInt(idText);
                    codeNames.add(playerName);
                    playerIds.add(playerID);
                }
            } catch (NumberFormatException ex) {
                System.err.println("Invalid input for player ID at entry " + (i + 1));
            }
        }

        //save the players from the green team
        for (int i = 0; i < 19; i++) {
            try {
                String playerName = greenTeamFields[i][1].getText().trim();
                String idText = greenTeamFields[i][0].getText().trim();
                
                if (!playerName.isEmpty() && !idText.isEmpty()) {
                    int playerID = Integer.parseInt(idText);

                    codeNames.add(playerName);
                    playerIds.add(playerID);
                }

            } catch (NumberFormatException ex) {
                System.err.println("Invalid input for Player ID at entry!" + (i + 1));
            }
        }

        // Add all of the Names and IDs to the db.
        for(int i = 0; i < codeNames.size(); i++){
            db.addplayer(codeNames.get(i), playerIds.get(i));
        }
    }

    private void changeIPAddress() {
        // Implement change IP address functionality
        // Create a dialog to enter the new IP address
        System.out.println("Change IP Address functionality triggered.");
        String newIP = "";
        // structure for IP address validation
        String ipPattern = "^((\\d{1,3})\\.){1,3}\\d{1,3}$";

        while (true) {
            // Prompt the user for a new IP address
            newIP = JOptionPane.showInputDialog(null, "Enter new IP Address (###.###.###.###):", "Change IP", JOptionPane.PLAIN_MESSAGE);
            if (newIP == null) {  // Cancel button pressed
                newIP = "127.0.0.1";
                break;
            }
            
            //error handling for incorrect inputs
            newIP = newIP.trim();
            if (newIP.isEmpty() || !newIP.matches(ipPattern)) {
                JOptionPane.showMessageDialog(null, "Invalid IP Address. Please enter in ###.###.###.### format.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                break;
            }
        }
       
        // Push the new IP address to udpBaseClient_2
        if (udpClient != null) {
            udpClient.setNetworkAddress(newIP);
        } else {
            System.err.println("udpBaseClient_2 instance is null. Cannot change IP address.");
        }
        //validate to console
        System.out.println("(entryscreen) New IP Address set to: " + newIP);
    }

    public void clearGame() {
        // implement clear game functionality
        System.out.println("Clear Game functionality triggered.");
    }

    public void flickSync() {
        // Implement flick sync functionality
        System.out.println("Flick Sync functionality triggered.");
    }

    public void viewGame() {
        // Implement view game functionality
        System.out.println("View Game functionality triggered.");
    }

    public void preEnteredGames() {
        // Implement pre-entered games functionality
        System.out.println("Pre-Entered Games functionality triggered.");
    }

    public void startGame() {
        // Implement start game functionality
        System.out.println("Start Game functionality triggered.");
        // Send a UDP packet to start the game
        if (udpClient != null) {
            udpClient.sendEquipmentID(202);
        } else {
            System.err.println("udpBaseClient_2 instance is null. Cannot start game.");
        }
    }

    public void gameParameters() {
        // Implement game parameters functionality
        System.out.println("Game Parameters functionality triggered.");
    }

    public void editGame() {
        // Implement edit game functionality
        System.out.println("Edit Game functionality triggered.");
    }
}
