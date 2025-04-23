import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.border.TitledBorder;

public class ResultWindow extends JFrame {

    public ResultWindow(int redScore, int greenScore) {
        this(redScore, greenScore, Map.of(), Map.of());
    }

    public ResultWindow(int redScore, int greenScore, Map<String, Integer> redPlayers, Map<String, Integer> greenPlayers) {
        setTitle("Game Results");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        String winningTeam, losingTeam;
        int winningScore, losingScore;
        Color winningColor, losingColor;

        if (redScore > greenScore) {
            winningTeam = "Red";
            winningScore = redScore;
            losingTeam = "Green";
            losingScore = greenScore;
            winningColor = Color.RED;
            losingColor = Color.GREEN;
        } else if (greenScore > redScore) {
            winningTeam = "Green";
            winningScore = greenScore;
            losingTeam = "Red";
            losingScore = redScore;
            winningColor = Color.GREEN;
            losingColor = Color.RED;
        } else {
            winningTeam = "It's a tie!";
            winningScore = redScore;
            losingTeam = "";
            losingScore = greenScore;
            winningColor = Color.GRAY;
            losingColor = Color.GRAY;
        }

        JLabel resultLabel = new JLabel();
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setVerticalAlignment(SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 24));

        if (winningTeam.equals("It's a tie!")) {
            resultLabel.setText("<html><center><b>It's a Tie!</b><br/>"
                    + "Red Score: " + redScore + "<br/>"
                    + "Green Score: " + greenScore + "</center></html>");
            resultLabel.setForeground(Color.GRAY);
        } else {
            resultLabel.setText("<html><center><b><span style='color:" + toHex(winningColor) + "'>"
                    + winningTeam + " Team Wins!</span></b><br/>"
                    + "<span style='color:" + toHex(winningColor) + "'>" + winningTeam + " Score: " + winningScore + "</span><br/>"
                    + "<span style='color:" + toHex(losingColor) + "'>" + losingTeam + " Score: " + losingScore + "</span></center></html>");
        }

        add(resultLabel, BorderLayout.NORTH);

        // Sort players by team and score
        List<Map.Entry<String, Integer>> sortedGreenPlayers = greenPlayers.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toList());

        List<Map.Entry<String, Integer>> sortedRedPlayers = redPlayers.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors.toList());

        // Create team panels
        JPanel greenTeamPanel = createTeamPanel("Green Team", sortedGreenPlayers, Color.GREEN);
        JPanel redTeamPanel = createTeamPanel("Red Team", sortedRedPlayers, Color.RED);

        // Combine team panels into a single panel
        JPanel teamsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        teamsPanel.add(greenTeamPanel);
        teamsPanel.add(redTeamPanel);

        add(teamsPanel, BorderLayout.CENTER);
    }

    private JPanel createTeamPanel(String teamName, List<Map.Entry<String, Integer>> players, Color teamColor) {
        JPanel teamPanel = new JPanel();
        teamPanel.setLayout(new BorderLayout(10, 10));
        teamPanel.setBackground(Color.BLACK);
        teamPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(teamColor, 2), teamName,
                TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 18), teamColor));

        JPanel playerListPanel = new JPanel();
        playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));
        playerListPanel.setBackground(Color.BLACK);

        for (Map.Entry<String, Integer> entry : players) {
            JLabel playerLabel = new JLabel(entry.getKey() + ": " + entry.getValue());
            playerLabel.setForeground(Color.WHITE);
            playerLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            playerLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            playerListPanel.add(playerLabel);
        }

        JScrollPane scrollPane = new JScrollPane(playerListPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.BLACK);

        teamPanel.add(scrollPane, BorderLayout.CENTER);
        return teamPanel;
    }

    private String toHex(Color color) {
        return String.format("#%02x%02x%02x",
                color.getRed(), color.getGreen(), color.getBlue());
    }
}