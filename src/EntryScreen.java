import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class EntryScreen {

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Entry Terminal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
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

    private JPanel createTeamPanel(String teamName, Color color) {
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

        for (int i = 1; i <= 19; i++) {
            JLabel label = new JLabel(" " + i, SwingConstants.RIGHT);
            label.setForeground(Color.WHITE);
            JTextField textField = new JTextField();
            entryPanel.add(label);
            entryPanel.add(textField);
        }

        teamPanel.add(entryPanel, BorderLayout.CENTER);
        return teamPanel;
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new GridLayout(1, 8, 5, 5));
        bottomPanel.setBackground(Color.BLACK);

        String[] buttonLabels = {
            "Edit Game (F1)", "Game Params (F2)", "Start Game (F3)", 
            "Prev. Games (F5)", "View Game (F7)", "Flick Sync (F10)", "Clear Game (F12)"
        };
        int[] keyBindings = {
            KeyEvent.VK_F1, KeyEvent.VK_F2, KeyEvent.VK_F3, KeyEvent.VK_F5, 
            KeyEvent.VK_F7, KeyEvent.VK_F10, KeyEvent.VK_F12
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = new JButton(buttonLabels[i]);
            button.setForeground(Color.GREEN);
            button.setBackground(Color.DARK_GRAY);
            button.setPreferredSize(new Dimension(100, 50));
            button.setMnemonic(keyBindings[i]);
            bottomPanel.add(button);
        }

        return bottomPanel;
    }
}
