import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import javax.swing.text.BadLocationException;

public class EntryScreen {
    // GUI components
    private JLabel redScoreLabel;
    private JLabel greenScoreLabel;
    private JPanel redTeamPanel;
    private JPanel greenTeamPanel;
    private JTextPane playActionPane;
    private Document doc;
    private Style regularStyle;
    private Style hitStyle;
    private Style baseHitStyle;
    private int redScore = 0;
    private int greenScore = 0;
    //creates two different 2d arrays to read data from both teams(red and blue)
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
        // Flags to ensure at least one valid entry exists for each team
        boolean redTeamHasPlayer = false;
        boolean greenTeamHasPlayer = false;
        
        // Process red team entries
        for (int i = 0; i < 15; i++) {
            try {
                String playerName = redTeamFields[i][1].getText().trim();
                String idText = redTeamFields[i][0].getText().trim();
                String hardwareIdText = redTeamFields[i][2].getText().trim();
                
                // Skip row if name or ID field is empty
                if (playerName.isEmpty() || idText.isEmpty()) {
                    continue;
                }
                
                // Validate that ID is numeric
                if (!isNumeric(idText)) {
                    JOptionPane.showMessageDialog(null, 
                            "Invalid input in User ID field (Red Team). Only numbers are allowed!", 
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    redTeamFields[i][0].setText("");
                    continue;
                }
                
                // Validate that Hardware ID (if provided) is numeric
                if (!hardwareIdText.isEmpty() && !isNumeric(hardwareIdText)) {
                    JOptionPane.showMessageDialog(null, 
                            "Invalid input in Hardware ID field (Red Team). Only numbers are allowed!", 
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    redTeamFields[i][2].setText("");
                    continue;
                }
                
                int playerID = Integer.parseInt(idText);
                int hardwareId = hardwareIdText.isEmpty() ? 0 : Integer.parseInt(hardwareIdText);
                
                // Add the red team player to the database
                db.addplayer(playerName, playerID, hardwareId, "Red");
                redTeamHasPlayer = true;
            } catch (NumberFormatException ex) {
                System.err.println("Invalid input for player ID at red team entry " + (i + 1));
            }
        }
        
        // Process green team entries
        for (int i = 0; i < 15; i++) {
            try {
                String playerName = greenTeamFields[i][1].getText().trim();
                String idText = greenTeamFields[i][0].getText().trim();
                String hardwareIdText = greenTeamFields[i][2].getText().trim();
                
                // Skip row if name or ID field is empty
                if (playerName.isEmpty() || idText.isEmpty()) {
                    continue;
                }
                
                // Validate that ID is numeric
                if (!isNumeric(idText)) {
                    JOptionPane.showMessageDialog(null, 
                            "Invalid input in User ID field (Green Team). Only numbers are allowed.", 
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    greenTeamFields[i][0].setText("");
                    continue;
                }
                
                // Validate that Hardware ID (if provided) is numeric
                if (!hardwareIdText.isEmpty() && !isNumeric(hardwareIdText)) {
                    JOptionPane.showMessageDialog(null, 
                            "Invalid input in Hardware ID field (Green Team). Only numbers are allowed.", 
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    greenTeamFields[i][2].setText("");
                    continue;
                }
                
                int playerID = Integer.parseInt(idText);
                int hardwareId = hardwareIdText.isEmpty() ? 0 : Integer.parseInt(hardwareIdText);
                
                // Add the green team player to the database
                db.addplayer(playerName, playerID, hardwareId, "Green");
                greenTeamHasPlayer = true;
            } catch (NumberFormatException ex) {
                System.err.println("Invalid input for player ID at green team entry " + (i + 1));
            }
        }
        
        // Check if at least one player was entered for each team
        if (!redTeamHasPlayer || !greenTeamHasPlayer) {
            JOptionPane.showMessageDialog(null, 
                    "At least one player must be entered for each team!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Retrieve updated entries from the database (if desired)
        db.retreiveEntries();
    }

    private void changeIPAddress() {
        // Implement change IP address functionality
        // Create a dialog to enter the new IP address
        System.out.println("Change IP Address functionality triggered!");
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

       //udp setup 
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
        System.out.println("Flick Sync functionality triggered!");
    }

    public void viewGame() {
        // Implement view game functionality
        System.out.println("View Game functionality triggered!");
    }

    public void preEnteredGames() {
        // Implement pre-entered games functionality
        System.out.println("Pre-Entered Games functionality triggered!");
    }

    public void startGame() {
        new Thread(() -> {
            // Countdown window
            JWindow countdownWindow = new JWindow();
            countdownWindow.setLayout(new BorderLayout());
            countdownWindow.getContentPane().setBackground(Color.BLACK);
    
            JLabel countdownLabel = new JLabel("3", SwingConstants.CENTER);
            countdownLabel.setFont(new Font("Arial", Font.BOLD, 100));
            countdownLabel.setForeground(Color.WHITE);
    
            JPanel countdownPanel = new JPanel(new BorderLayout());
            countdownPanel.setBackground(Color.BLACK);
            countdownPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 10));
            countdownPanel.add(countdownLabel, BorderLayout.CENTER);
    
            countdownWindow.add(countdownPanel, BorderLayout.CENTER);
    
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) (screenSize.width * 0.8);
            int height = (int) (screenSize.height * 0.8);
    
            countdownWindow.setSize(width, height);
            countdownWindow.setLocation((screenSize.width - width) / 2, (screenSize.height - height) / 2);
            countdownWindow.setVisible(true);
    
            final Thread[] musicThread = new Thread[1];
            for (int i = 30; i > -1; i--) {
                countdownLabel.setText(String.valueOf(i));
                try {
                    if (i == 16) {
                        musicThread[0] = new Thread(() -> {
                            music_player.play_random_track();
                        });
                        musicThread[0].start();
                    }
                    if (i == 0) {
                        countdownLabel.setText("Begin!");
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countdownWindow.setVisible(false);
            countdownWindow.dispose();
            udpClient.sendEquipmentID(202);
    
            // Set up player window
            JFrame playerWindow = new JFrame("Entered Players");
            playerWindow.setLayout(new BorderLayout());
    
            playerWindow.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    udpClient.sendEquipmentID(221);
                    if (musicThread[0] != null && musicThread[0].isAlive()) {
                        music_player.stopPlayback();
                    }
                    playerWindow.dispose();
                }
            });
    
            // Team panels
            JPanel redTeamPanel = new JPanel();
            redTeamPanel.setLayout(new BoxLayout(redTeamPanel, BoxLayout.Y_AXIS));
            redTeamPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.RED, 2), "RED TEAM",
                    TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24), Color.RED));
            redTeamPanel.setBackground(Color.BLACK);
    
            JPanel greenTeamPanel = new JPanel();
            greenTeamPanel.setLayout(new BoxLayout(greenTeamPanel, BoxLayout.Y_AXIS));
            greenTeamPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.GREEN, 2), "GREEN TEAM",
                    TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24), Color.GREEN));
            greenTeamPanel.setBackground(Color.BLACK);
    
            try {
                ResultSet resultSet = db.retreiveEntries();
                while (resultSet.next()) {
                    String codename = resultSet.getString("codename".trim());
                    String team = resultSet.getString("team".trim());
    
                    JPanel playerPanel = new JPanel();
                    playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
                    playerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                    playerPanel.setBackground(Color.BLACK);
    
                    JLabel codenameLabel = new JLabel(codename);
                    codenameLabel.setForeground(Color.WHITE);
                    codenameLabel.setFont(new Font("Arial", Font.BOLD, 12));
                    playerPanel.add(codenameLabel);
    
                    if ("red".equalsIgnoreCase(team)) {
                        redTeamPanel.add(playerPanel);
                        redTeamPanel.add(Box.createVerticalStrut(10));
                    } else if ("green".equalsIgnoreCase(team)) {
                        greenTeamPanel.add(playerPanel);
                        greenTeamPanel.add(Box.createVerticalStrut(10));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    
            // Live score labels
            redScore = 0;
            greenScore = 0;
            redScoreLabel = new JLabel("Red Score: 0", SwingConstants.CENTER);
            greenScoreLabel = new JLabel("Green Score: 0", SwingConstants.CENTER);
            redScoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
            greenScoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
            redScoreLabel.setForeground(Color.RED);
            greenScoreLabel.setForeground(Color.GREEN);
            redScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            greenScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
            // Timer label
            JLabel timerLabel = new JLabel("06:00", SwingConstants.CENTER);
            timerLabel.setFont(new Font("Arial", Font.BOLD, 36));
            timerLabel.setForeground(Color.WHITE);
            timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    
            // Play action pane
            playActionPane = new JTextPane();
            playActionPane.setEditable(false);
            playActionPane.setBackground(Color.BLACK);
            doc = playActionPane.getStyledDocument();
    
            // Define styles
            regularStyle = playActionPane.addStyle("Regular", null);
            StyleConstants.setForeground(regularStyle, Color.WHITE);
            StyleConstants.setFontSize(regularStyle, 14);
            StyleConstants.setFontFamily(regularStyle, "Monospaced");
    
            hitStyle = playActionPane.addStyle("Hit", null);
            StyleConstants.setForeground(hitStyle, Color.YELLOW);
            StyleConstants.setFontSize(hitStyle, 16);
            StyleConstants.setBold(hitStyle, true);
    
            baseHitStyle = playActionPane.addStyle("BaseHit", null);
            StyleConstants.setForeground(baseHitStyle, Color.CYAN);
            StyleConstants.setFontSize(baseHitStyle, 16);
            StyleConstants.setBold(baseHitStyle, true);
    
            JScrollPane actionScroll = new JScrollPane(playActionPane);
            actionScroll.setPreferredSize(new Dimension(400, 150));
            actionScroll.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            actionScroll.setBackground(Color.BLACK);
    
            JPanel gameActionPanel = new JPanel();
            gameActionPanel.setLayout(new BoxLayout(gameActionPanel, BoxLayout.Y_AXIS));
            gameActionPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.WHITE, 2), "Current Game Action",
                    TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 24), Color.WHITE));
            gameActionPanel.setBackground(Color.BLUE);
            gameActionPanel.add(timerLabel);
            gameActionPanel.add(Box.createVerticalStrut(10));
            gameActionPanel.add(redScoreLabel);
            gameActionPanel.add(greenScoreLabel);
            gameActionPanel.add(Box.createVerticalStrut(10));
            gameActionPanel.add(actionScroll);
    
            JPanel teamsPanel = new JPanel(new GridLayout(1, 2));
            teamsPanel.add(redTeamPanel);
            teamsPanel.add(greenTeamPanel);
    
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(teamsPanel);
            mainPanel.add(Box.createVerticalStrut(20));
            mainPanel.add(gameActionPanel);
    
            playerWindow.add(mainPanel, BorderLayout.CENTER);
            playerWindow.setSize(700, 700);
            playerWindow.setLocationRelativeTo(null);
            playerWindow.setVisible(true);
    
            // Start game timer
            new Thread(() -> {
                int totalSeconds = 360;
    
                for (int i = totalSeconds; i >= 0; i--) {
                    int minutes = i / 60;
                    int seconds = i % 60;
                    String timeFormatted = String.format("%02d:%02d", minutes, seconds);
                    SwingUtilities.invokeLater(() -> timerLabel.setText(timeFormatted));
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
    
                SwingUtilities.invokeLater(() -> timerLabel.setText("Game Over!"));
                udpClient.sendEquipmentID(221);
                System.out.println("Stop Traffic!");
                System.out.println("Game Has Ended!");
    
                if (musicThread[0] != null && musicThread[0].isAlive()) {
                    music_player.stopPlayback();
                }
    
                playerWindow.dispose();
            }).start();
    
        }).start();
    }

        // Method for updating Scores on Base Server
        public void updateScores(int redScore, int greenScore) {
            // Update the score labels
            SwingUtilities.invokeLater(() -> {
                redScoreLabel.setText("Red Score: " + redScore);
                greenScoreLabel.setText("Green Score: " + greenScore);
            });
        }
    
        public void appendPlayAction(String message) {
            SwingUtilities.invokeLater(() -> {
                try {
                    Style styleToUse = regularStyle;
                    String lowerMsg = message.toLowerCase();
        
                    // Update score based on team and event
                    if (lowerMsg.contains("red") && lowerMsg.contains("hit")) {
                        redScore += lowerMsg.contains("base") ? 10 : 1;
                        redScoreLabel.setText("Red Score: " + redScore);
                        styleToUse = lowerMsg.contains("base") ? baseHitStyle : hitStyle;
        
                    } else if (lowerMsg.contains("green") && lowerMsg.contains("hit")) {
                        greenScore += lowerMsg.contains("base") ? 10 : 1;
                        greenScoreLabel.setText("Green Score: " + greenScore);
                        styleToUse = lowerMsg.contains("base") ? baseHitStyle : hitStyle;
                    }
        
                    doc.insertString(doc.getLength(), message + "\n", styleToUse);
                    playActionPane.setCaretPosition(doc.getLength());
        
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            });
        }
    
    public void gameParameters() {
        // Implement game parameters functionality - might do later
        System.out.println("Game Parameters functionality triggered!");
    }

    public void editGame() {
        // Implement edit game functionality
        System.out.println("Edit Game functionality triggered!");
    }
}