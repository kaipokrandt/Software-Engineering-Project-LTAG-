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


    //implement database functionality to connnect
    public database db = new database();

    public void setDB(database db){
        this.db = db;
    }

    //creates base for user GUI
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Entry Terminal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 600);
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
    public JPanel createTeamPanel(String teamName, Color color, boolean isRedTeam) {
        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new BorderLayout());
        teamPanel.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel(teamName, SwingConstants.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(color);
        titleLabel.setForeground(Color.WHITE);
        teamPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel entryPanel = new JPanel(new GridLayout(20, 2, 5, 5));
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

        JTextField[][] playerFields = new JTextField[19][2];

        for (int i = 0; i < 19; i++) {
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

    //create bottom buttons with keybind functionality
    public JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 9, 5, 5));
        bottomPanel.setBackground(Color.BLACK);

        // Add buttons using the helper method.
        bottomPanel.add(createButton("Edit Game (F1)", KeyEvent.VK_F1, e -> {
                System.out.println("Edit Game button clicked");
                editGame();
            }
        });

        JButton gameParametersButton = new JButton("Game Parameters (F2)");
        gameParametersButton.setForeground(Color.GREEN);
        gameParametersButton.setBackground(Color.DARK_GRAY);
        gameParametersButton.setPreferredSize(new Dimension(100, 50));
        gameParametersButton.setMnemonic(KeyEvent.VK_F2);
        bottomPanel.add(gameParametersButton);
        // Add action listeners for the buttons
        gameParametersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the functionality for editing the game
                System.out.println("Game Parameters button clicked");
                gameParameters();
            }
        });

        JButton startGameButton = new JButton("Start Game (F3)");
        startGameButton.setForeground(Color.GREEN);
        startGameButton.setBackground(Color.DARK_GRAY);
        startGameButton.setPreferredSize(new Dimension(100, 50));
        startGameButton.setMnemonic(KeyEvent.VK_F3);
        bottomPanel.add(startGameButton);
        // Add action listeners for the buttons
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the functionality for editing the game
                System.out.println("Start Game button clicked");
                startGame();
            }
        });

        JButton preEnteredGamesButton = new JButton("PreEntered Games (F5)");
        preEnteredGamesButton.setForeground(Color.GREEN);
        preEnteredGamesButton.setBackground(Color.DARK_GRAY);
        preEnteredGamesButton.setPreferredSize(new Dimension(100, 50));
        preEnteredGamesButton.setMnemonic(KeyEvent.VK_F5);
        bottomPanel.add(preEnteredGamesButton);
        // Add action listeners for the buttons
        preEnteredGamesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the functionality for editing the game
                System.out.println("PreEntered Game button clicked");
                preEnteredGames();
            }
        });

        JButton viewGameButton = new JButton("View Game (F7)");
        viewGameButton.setForeground(Color.GREEN);
        viewGameButton.setBackground(Color.DARK_GRAY);
        viewGameButton.setPreferredSize(new Dimension(100, 50));
        viewGameButton.setMnemonic(KeyEvent.VK_F7);
        bottomPanel.add(viewGameButton);
        // Add action listeners for the buttons
        viewGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the functionality for editing the game
                System.out.println("View Game button clicked");
                viewGame();
            }
        });

        JButton flickSyncButton = new JButton("Flick Sync (F10)");
        flickSyncButton.setForeground(Color.GREEN);
        flickSyncButton.setBackground(Color.DARK_GRAY);
        flickSyncButton.setPreferredSize(new Dimension(100, 50));
        flickSyncButton.setMnemonic(KeyEvent.VK_F10);
        bottomPanel.add(flickSyncButton);
        // Add action listeners for the buttons
        flickSyncButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the functionality for editing the game
                System.out.println("Flick Sync button clicked");
                flickSync();
            }
        });

        JButton changeIPButton = new JButton("Change IP (F11)");
        changeIPButton.setForeground(Color.GREEN);
        changeIPButton.setBackground(Color.DARK_GRAY);
        changeIPButton.setPreferredSize(new Dimension(100, 50));
        changeIPButton.setMnemonic(KeyEvent.VK_F11);
        bottomPanel.add(changeIPButton);
        // Add action listeners for the buttons
        changeIPButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the functionality for editing the game
                System.out.println("Change IP button clicked");
                changeIPAddress();
            }
        });


        JButton clearButton = new JButton("Clear Game (F12)");
        clearButton.setForeground(Color.GREEN);
        clearButton.setBackground(Color.DARK_GRAY);
        clearButton.setPreferredSize(new Dimension(100, 50));
        clearButton.setMnemonic(KeyEvent.VK_F12);
        bottomPanel.add(clearButton);
        // Add action listeners for the buttons
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the functionality for editing the game
                System.out.println("Clear Game button clicked");
                clearGame();
            }
        });

        JButton submitButton = new JButton("Submit");
        submitButton.setForeground(Color.GREEN);
        submitButton.setBackground(Color.DARK_GRAY);
        submitButton.setPreferredSize(new Dimension(100, 50));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Implement the functionality for starting the game
                System.out.println("Submit player button clicked");
                savePlayersToDatabase();
            }
        });
        bottomPanel.add(submitButton);

        return bottomPanel;
    }

    public void savePlayersToDatabase() {

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

    public void changeIPAddress() {
        // Create a dialog to enter the new IP address
        String newIP = JOptionPane.showInputDialog(null, "Enter new IP address:", "Change IP Address", JOptionPane.PLAIN_MESSAGE);
        
        if (newIP != null && !newIP.trim().isEmpty()) {
            // Here you would add the code to change the IP address in your application
            System.out.println("New IP address set to: " + newIP);
        } else {
            System.out.println("No valid IP address entered.");
        }
    }

    public void clearGame() {
        // Clear all text fields in both teams
        for (int i = 0; i < 19; i++) {
            redTeamFields[i][0].setText("");
            redTeamFields[i][1].setText("");
            greenTeamFields[i][0].setText("");
            greenTeamFields[i][1].setText("");
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
        // Implement start game functionality
        System.out.println("Start Game functionality triggered.");
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
