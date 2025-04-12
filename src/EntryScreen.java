import javax.crypto.spec.PBEKeySpec;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.concurrent.Flow;

public class EntryScreen {

    //creates two different 2d arrays to read data from both teams
    private JTextField[][] redTeamFields;
    private JTextField[][] greenTeamFields;

    private ArrayList<String> redTeamCodeNames = new ArrayList<String>();
    private ArrayList<String> greenTeamCodeNames = new ArrayList<String>();
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
        JLabel hardwareHeader = new JLabel("Hardware ID", SwingConstants.CENTER);
        hardwareHeader.setForeground(Color.WHITE);  // Ensure the header text is white

        entryPanel.add(entryHeader); // Player entry number column header
        entryPanel.add(idHeader);    // ID column header
        entryPanel.add(nameHeader);  // Name column header
        entryPanel.add(hardwareHeader); // Hardware ID column header

        JTextField[][] playerFields = new JTextField[15][3];

        //create text fields for player entry
        //create text fields for player entry
        for (int i = 0; i < 15; i++) {
            JLabel label = new JLabel(String.valueOf(i + 1).trim(), SwingConstants.CENTER);
            label.setForeground(Color.WHITE);
            JTextField idField = new JTextField();;
            JTextField nameField = new JTextField();
            JTextField hardwareField = new JTextField();
            entryPanel.add(label);
            entryPanel.add(idField);
            entryPanel.add(nameField);
            entryPanel.add(hardwareField);
            playerFields[i][0] = idField;
            playerFields[i][1] = nameField;
            playerFields[i][2] = hardwareField;
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
    
        // Add buttons and descriptions using the helper method.
        bottomPanel.add(createButton("Edit Game (F1)", KeyEvent.VK_F1, e -> editGame()));
        bottomPanel.add(createButton("Game Params (F2)", KeyEvent.VK_F2, e -> gameParameters()));
        bottomPanel.add(createButton("View Game (F3)", KeyEvent.VK_F3, e -> viewGame()));
        bottomPanel.add(createButton("PreEnt. Games (F4)", KeyEvent.VK_F4, e -> preEnteredGames()));
        bottomPanel.add(createButton("Start Game (F5)", KeyEvent.VK_F5, e -> startGame()));
        bottomPanel.add(createButton("Flick Sync (F6)", KeyEvent.VK_F6, e -> flickSync()));
        bottomPanel.add(createButton("Change IP (F7)", KeyEvent.VK_F7, e -> changeIPAddress()));
        bottomPanel.add(createButton("Clear Game (F12)", KeyEvent.VK_F8, e -> clearGame()));
        bottomPanel.add(createButton("Submit (F8)", KeyEvent.VK_F12, e -> savePlayersToDatabase()));
        
        // keybind remapping
        // Bind the function keys to the panel
        bindKeyToAction(bottomPanel, KeyEvent.VK_F1, "editGame", this::editGame);
        bindKeyToAction(bottomPanel, KeyEvent.VK_F2, "gameParams", this::gameParameters);
        bindKeyToAction(bottomPanel, KeyEvent.VK_F3, "viewGame", this::viewGame);
        bindKeyToAction(bottomPanel, KeyEvent.VK_F4, "preEnteredGames", this::preEnteredGames);
        bindKeyToAction(bottomPanel, KeyEvent.VK_F5, "startGame", this::startGame);
        bindKeyToAction(bottomPanel, KeyEvent.VK_F6, "flickSync", this::flickSync);
        bindKeyToAction(bottomPanel, KeyEvent.VK_F7, "changeIPAddress", this::changeIPAddress);
        bindKeyToAction(bottomPanel, KeyEvent.VK_F12, "clearGame", this::clearGame);
        bindKeyToAction(bottomPanel, KeyEvent.VK_F8, "submitPlayers", this::savePlayersToDatabase);
        
        return bottomPanel;
    }
    
    // Helper method for binding keys to different actions
    private void bindKeyToAction(JComponent component, int keyCode, String actionName, Runnable action) {
        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();
    
        inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), actionName);
        actionMap.put(actionName, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
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

    //check for numeric entry (for ID and Hardware ID)
    private boolean isNumeric(String input) {
        return input.matches("\\d+"); // Matches digits
    }

    private void savePlayersToDatabase() {
        // Check if at least one player is entered for both teams
        boolean redTeamHasPlayer = false;
        boolean greenTeamHasPlayer = false;
    
        for (int i = 0; i < 15; i++) {
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
        ArrayList<Integer> hardwareIds = new ArrayList<Integer>();
        ArrayList<String> teams = new ArrayList<String>();
    
        // Save players from the red team
        for (int i = 0; i < 15; i++) {
            try{
            
                String playerName = redTeamFields[i][1].getText().trim();
                String idText = redTeamFields[i][0].getText().trim();
                String hardwareIdtext = redTeamFields[i][2].getText().trim();
                
                if (!idText.isEmpty() && !isNumeric(idText)) {
                    JOptionPane.showMessageDialog(null, "Invalid input in User ID field. Only numbers are allowed.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    redTeamFields[i][0].setText(""); // Clear the field
                }
                if (!hardwareIdtext.isEmpty() && !isNumeric(hardwareIdtext)) {
                    JOptionPane.showMessageDialog(null, "Invalid input in Hardware ID field. Only numbers are allowed.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    redTeamFields[i][2].setText(""); // Clear the field
                    continue;
                }

                if (!playerName.isEmpty() && !idText.isEmpty()) {
                    int playerID = Integer.parseInt(idText);
                    
                    int hardwareId = Integer.parseInt(hardwareIdtext);
                    codeNames.add(playerName);
                    System.out.println(playerName);
                    playerIds.add(playerID);
                    hardwareIds.add(hardwareId);
                    redTeamCodeNames.add(playerName);
                    System.out.println("Red Team names: " + redTeamCodeNames.get(0) );
                    teams.add("Red");
                }
            } catch (NumberFormatException ex) {
               System.err.println("Invalid input for player ID at entry " + (i + 1));
            }
        }
    
        // Save the players from the green team
        for (int i = 0; i < 15; i++) {
            try {
                String playerName = greenTeamFields[i][1].getText().trim();
                String idText = greenTeamFields[i][0].getText().trim();
                String hardwareIdtext = greenTeamFields[i][2].getText().trim();
                
                if (!idText.isEmpty() && !isNumeric(idText)) {
                    JOptionPane.showMessageDialog(null, "Invalid input in User ID field. Only numbers are allowed.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    greenTeamFields[i][0].setText(""); // Clear the field
                }
                if (!hardwareIdtext.isEmpty() && !isNumeric(hardwareIdtext)) {
                    JOptionPane.showMessageDialog(null, "Invalid input in Hardware ID field. Only numbers are allowed.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    greenTeamFields[i][2].setText(""); // Clear the field
                    continue;
                }

                if (!playerName.isEmpty() && !idText.isEmpty()) {
                    int playerID = Integer.parseInt(idText);
                    int hardwareId = Integer.parseInt(hardwareIdtext);
    
                    codeNames.add(playerName);
                    System.out.println(playerName);
                    playerIds.add(playerID);
                    hardwareIds.add(hardwareId);
                    greenTeamCodeNames.add(playerName);
                    System.out.println("Green Team names: " + greenTeamCodeNames.get(0) );
                    teams.add("Green");
                }
    
            } catch (NumberFormatException ex) {
                System.err.println("Invalid input for Player ID at entry!" + (i + 1));
            }
        }

        for(int i = 0; i < codeNames.size(); i++){
            System.out.println(codeNames.get(i));
        }
    
        // Add all the Names and IDs to the db
        for (int i = 0; i < codeNames.size(); i++) {
            int playerID = playerIds.get(i);
            String playerName = codeNames.get(i);
    
            // Check if the player ID already exists
            String existingUserName = db.getUserNameByID(playerID);
            if (existingUserName != null && !existingUserName.equals(playerName)) {
                // Show a popup to notify the user of the username change
                JOptionPane.showMessageDialog(null, "Duplicate UserID detected. The username has been changed to: " + existingUserName, 
                        "Duplicate UserID", JOptionPane.INFORMATION_MESSAGE);
                redTeamCodeNames.clear();
                greenTeamCodeNames.clear();
                // Update the player entry screen with the existing username
                if (teams.get(i).equals("Red")) {
                    redTeamFields[i][1].setText(existingUserName); // Update red team entry
                } else {
                    greenTeamFields[i-1][1].setText(existingUserName); // Update green team entry
                } 
            } else if(redTeamCodeNames.size() > 0 && greenTeamCodeNames.size() > 0) {
                // Add player to the database if there is no duplicate
                for(int j = 0; j < redTeamCodeNames.size(); j++){
                    if(redTeamCodeNames.get(j).equals(playerName)){
                        db.addplayer(playerName, playerID, hardwareIds.get(i),"Red");
                    }
                }
                
                for(int j = 0; j < greenTeamCodeNames.size(); j++){
                    if(greenTeamCodeNames.get(j).equals(playerName)){
                        db.addplayer(playerName, playerID, hardwareIds.get(i),"Green");
                    }
                }
                
            }
        }
    
        // Retrieve updated entries from the database
        db.retreiveEntries();
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
        // Show confirmation dialog to the user for clearing the database
        int confirm = JOptionPane.showConfirmDialog(
            null, 
            "DEBUG ONLY! : Do you want to clear the database along with the player entries?", 
            "Clear Game", 
            JOptionPane.YES_NO_OPTION
        );
    
        // Always clear red team fields
        for (int i = 0; i < 15; i++) {
            redTeamFields[i][0].setText(""); // Clear ID field
            redTeamFields[i][1].setText(""); // Clear Name field
            redTeamFields[i][2].setText(""); // Clear Hardware ID field
        }
    
        // Always clear green team fields
        for (int i = 0; i < 15; i++) {
            greenTeamFields[i][0].setText(""); // Clear ID field
            greenTeamFields[i][1].setText(""); // Clear Name field
            greenTeamFields[i][2].setText(""); // Clear Hardware ID field
        }
    
        // If user selects YES, clear the database as well
        if (confirm == JOptionPane.YES_OPTION) {
            // Clear the database
            db.clearTable();
    
            // Show confirmation message for clearing both
            JOptionPane.showMessageDialog(
                null, 
                "Player entries and database have been cleared.", 
                "Clear Game", 
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            // Show message confirming only player entries were cleared
            JOptionPane.showMessageDialog(
                null, 
                "Player entries have been cleared, but database was not affected.", 
                "Clear Game", 
                JOptionPane.INFORMATION_MESSAGE
            );
        }
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
    new Thread(() -> {
        // Create the countdown window
        JWindow countdownWindow = new JWindow();
        countdownWindow.setLayout(new BorderLayout());

        // Set background color to black for the window
        countdownWindow.getContentPane().setBackground(Color.BLACK);

        // Create a label to display the countdown
        JLabel countdownLabel = new JLabel("3", SwingConstants.CENTER);
        countdownLabel.setFont(new Font("Arial", Font.BOLD, 100));
        countdownLabel.setForeground(Color.WHITE);  // White text for contrast

        // Create a panel for the countdown label with a border (outline)
        JPanel countdownPanel = new JPanel(new BorderLayout());
        countdownPanel.setBackground(Color.BLACK); // Make panel background black
        countdownPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 10)); // Set white border with 10px thickness
        countdownPanel.add(countdownLabel, BorderLayout.CENTER);

        // Add the panel to the countdown window
        countdownWindow.add(countdownPanel, BorderLayout.CENTER);

        // Get screen size for centering the window and scaling it to 80%
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) (screenSize.width * 0.8);  // 80% of screen width
        int height = (int) (screenSize.height * 0.8); // 80% of screen height

        countdownWindow.setSize(width, height);
        countdownWindow.setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2); // Center the window

        // Make the countdown window visible
        countdownWindow.setVisible(true);

        Thread musicThread = null;
        // Start the countdown
        for (int i = 18; i > -1; i--) {
            countdownLabel.setText(String.valueOf(i));

           
            try {

                if(i == 16) {
                    musicThread = new Thread(()-> {
                        try {
                            music_player.play_random_track(Thread.currentThread());
                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    });

                    musicThread.start();
                }
                if(i == 0){
                    countdownLabel.setText("Begin!");
                }
                Thread.sleep(1000); 
                
                 // Wait for 1 second before updating the countdown
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            
        }


        // After the countdown, close the window
        countdownWindow.setVisible(false);
        countdownWindow.dispose();

        udpClient.sendEquipmentID(202);

        // Retrieve players from the database
        ResultSet resultSet = db.retreiveEntries();

        // Create a new window to display the entered players
        JFrame playerWindow = new JFrame("Entered Players");
        playerWindow.setLayout(new BorderLayout());  // Use BorderLayout for better separation of components
        
        playerWindow.addWindowListener(new WindowAdapter() {
            
            public void windowClosing(WindowEvent e){

                udpClient.sendEquipmentID(221);
                playerWindow.dispose();
            }   
        });

        // Create a panel for both Red and Green teams
        JPanel teamsPanel = new JPanel();
        teamsPanel.setLayout(new GridLayout(1, 2));  // 1 rows: one for each team

        // Create panel for Red Team with additional styling
        JPanel redTeamPanel = new JPanel();
        redTeamPanel.setLayout(new BoxLayout(redTeamPanel, BoxLayout.Y_AXIS)); // Use BoxLayout for better alignment
        redTeamPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED, 2), "RED TEAM", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24), Color.RED));
        redTeamPanel.setBackground(Color.BLACK);

        // Create panel for Green Team with similar styling
        JPanel greenTeamPanel = new JPanel();
        greenTeamPanel.setLayout(new BoxLayout(greenTeamPanel, BoxLayout.Y_AXIS));
        greenTeamPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GREEN, 2), "GREEN TEAM", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24), Color.GREEN));
        greenTeamPanel.setBackground(Color.BLACK);
        

        db.retreiveEntries();
        try {
            // Iterate through the ResultSet and add players to the respective team panels
            while (resultSet.next()) {
                int id = resultSet.getInt("id".trim());
                String codename = resultSet.getString("codename".trim());

                String codeNameinDb = null;

                for(int i = 0; i < redTeamCodeNames.size(); i++){
                    if(codename.equals(redTeamCodeNames.get(i))){
                        codeNameinDb = codename;
                    }
                }

                for(int i = 0; i < greenTeamCodeNames.size(); i++){
                    if(codename.equals(greenTeamCodeNames.get(i))){
                        codeNameinDb = codename;
                    }
                }
                
                
                String team = resultSet.getString("team".trim());  // Assuming 'team' column to distinguish teams

                // Create a panel for the player
                JPanel playerPanel = new JPanel();
                playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
                playerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                playerPanel.setBackground(Color.BLACK);

                

                JLabel codenameLabel = new JLabel(codeNameinDb);
                codenameLabel.setForeground(Color.WHITE);
                codenameLabel.setFont(new Font("Arial", Font.BOLD, 12));
                playerPanel.add(codenameLabel);



                // Add player to the appropriate team panel based on the team column
                if ("Red".equals(team)) {
                    redTeamPanel.add(playerPanel);
                    redTeamPanel.add(Box.createVerticalStrut(10));
                } else if ("Green".equals(team)) {
                    greenTeamPanel.add(playerPanel);
                    greenTeamPanel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //current game action panel
        JPanel gameActioPanel = new JPanel();
        gameActioPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        gameActioPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2), "Current Game Action", TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24), Color.WHITE));
        gameActioPanel.setBackground(Color.BLUE);

        // Add the Red and Green team panels to the teamsPanel
        teamsPanel.add(redTeamPanel);
        teamsPanel.add(greenTeamPanel);


        //create a main panel to hold both panels 
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(teamsPanel);
        mainPanel.add(Box.createVerticalStrut(20)); //add vertical space between two panels
        mainPanel.add(gameActioPanel);



        // Add the teamsPanel to the player window
        playerWindow.add(mainPanel, BorderLayout.CENTER);

        // Set window size and location
        playerWindow.setSize(600, 600);  // Increased height to accommodate both teams
        playerWindow.setLocationRelativeTo(null);  // Center the window

        // Make the player window visible
        playerWindow.setVisible(true);

        

    }).start();
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
