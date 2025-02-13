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
        JPanel bottomPanel = new JPanel(new GridLayout(1, 8, 5, 5));
        bottomPanel.setBackground(Color.BLACK);

        String[] buttonLabels = {"Edit Game (F1)", "Game Parameters (F2)", "Start Game (F3)", "PreEntered Games (F5)", "View Game (F7)", "Flick Sync (F10)", "Clear Game (F12)", "Submit"};
        int[] keyBindings = {KeyEvent.VK_F1, KeyEvent.VK_F2, KeyEvent.VK_F3, KeyEvent.VK_F5, KeyEvent.VK_F7, KeyEvent.VK_F10, KeyEvent.VK_F12};

        for (int i = 0; i < buttonLabels.length - 1; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setForeground(Color.GREEN);
            button.setBackground(Color.DARK_GRAY);
            button.setPreferredSize(new Dimension(100, 50));
            button.setMnemonic(keyBindings[i]);
            bottomPanel.add(button);
        }

        JButton submitButton = new JButton("Submit");
        submitButton.setForeground(Color.GREEN);
        submitButton.setBackground(Color.DARK_GRAY);
        submitButton.setPreferredSize(new Dimension(100, 50));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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


        
        ArrayList<Integer> InvalidPlayerIds = new ArrayList<Integer>();
        //save the players from the red team
        for (int i = 0; i < 19; i++) {
            try {
                String playerName = redTeamFields[i][1].getText().trim();
                String idText = redTeamFields[i][0].getText().trim();
                
                if (!playerName.isEmpty() && !idText.isEmpty()) {
                    int playerID = Integer.parseInt(idText);
                    db.addplayer(playerName, playerID);
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

                    System.out.println("Checking" + database.checkIfIdExists(playerID));
                    if(database.checkIfIdExists(playerID)){
                        InvalidPlayerIds.add(playerID);
                        System.out.println(playerID + " already exists in the database.");
                    }
                    else{
                        db.addplayer(playerName, playerID);
                    }
                }

            } catch (NumberFormatException ex) {
                System.err.println("Invalid input for Player ID at entry!" + (i + 1));
            }
        }

        if(InvalidPlayerIds.size() > 0){
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < InvalidPlayerIds.size(); i++) {
                sb.append(InvalidPlayerIds.get(i));
                if(i != InvalidPlayerIds.size() -1){
                    sb.append(", ");
                }
            }
            JOptionPane.showMessageDialog(null, "The following Player IDs are already in use: " + sb.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
