import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class EntryScreen {
    private JTextField[][] playerFields;
    //implement database functionality


    //creates base for user GUI
    public void createAndShowGUI() {
        JFrame frame = new JFrame("Entry Terminal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        JPanel redTeamPanel = createTeamPanel("RED TEAM", Color.RED);
        JPanel greenTeamPanel = createTeamPanel("GREEN TEAM", Color.GREEN);

        mainPanel.add(redTeamPanel);
        mainPanel.add(greenTeamPanel);

        JPanel bottomPanel = createBottomPanel();

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    //create team entry panel with player entry fields
    public JPanel createTeamPanel(String teamName, Color color) {
        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new BorderLayout());
        teamPanel.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel(teamName, SwingConstants.CENTER);
        titleLabel.setOpaque(true);
        titleLabel.setBackground(color);
        titleLabel.setForeground(Color.WHITE);
        teamPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel entryPanel = new JPanel(new GridLayout(19, 2, 5, 5));
        entryPanel.setBackground(Color.BLACK);
        
        playerFields = new JTextField[19][2];

        for (int i = 0; i < 19; i++) {
            JLabel label = new JLabel(" " + (i + 1), SwingConstants.RIGHT);
            label.setForeground(Color.WHITE);
            JTextField idField = new JTextField();
            JTextField nameField = new JTextField();
            entryPanel.add(label);
            entryPanel.add(idField);
            entryPanel.add(nameField);
            playerFields[i][0] = idField;
            playerFields[i][1] = nameField;
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
            button.setPreferredSize(new Dimension(100, 70));
            button.setMnemonic(keyBindings[i]);
            bottomPanel.add(button);
        }

        JButton submitButton = new JButton("Submit");
        submitButton.setForeground(Color.GREEN);
        submitButton.setBackground(Color.DARK_GRAY);
        submitButton.setPreferredSize(new Dimension(100, 70));
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
            String redPlayerName = playerFields[i][1].getText().trim();
            String greenPlayerName = playerFields[i][1].getText().trim();
            
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

        //save players to db
        for (int i = 0; i < 19; i++) {
            try {
                String playerName = playerFields[i][1].getText().trim();
                String idText = playerFields[i][0].getText().trim();
                
                if (!playerName.isEmpty() && !idText.isEmpty()) {
                    int playerID = Integer.parseInt(idText);
                    //database.addplayer(playerName, playerID);
                }
            } catch (NumberFormatException ex) {
                System.err.println("Invalid input for player ID at entry " + (i + 1));
            }
        }
    }
}
