import javax.swing.*;
import java.awt.*;

public class ResultWindow extends JFrame {

    public ResultWindow(String winningTeam, int winningScore, String losingTeam, int losingScore) {
        setTitle("Game Results");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        Color winningColor = Color.WHITE;
        Color losingColor = Color.WHITE;

        if (winningTeam.equalsIgnoreCase("Red")) {
            winningColor = Color.RED;
            losingColor = Color.GREEN;
        } else if (winningTeam.equalsIgnoreCase("Green")) {
            winningColor = Color.GREEN;
            losingColor = Color.RED;
        }

        JLabel titleLabel;
        JLabel winnerLabel = new JLabel();
        JLabel loserLabel = new JLabel();

        if (losingTeam.equals("")) {
            // It's a tie
            titleLabel = new JLabel("It's a Tie!", SwingConstants.CENTER);
            titleLabel.setForeground(Color.YELLOW);
            winnerLabel.setText("Both Teams Score: " + winningScore);
            winnerLabel.setForeground(Color.WHITE);
            loserLabel.setText(""); // No losing team
        } else {
            titleLabel = new JLabel(winningTeam + " Team Wins!", SwingConstants.CENTER);
            titleLabel.setForeground(winningColor);
            winnerLabel.setText(winningTeam + " Score: " + winningScore);
            winnerLabel.setForeground(winningColor);
            loserLabel.setText(losingTeam + " Score: " + losingScore);
            loserLabel.setForeground(losingColor);
        }

        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        winnerLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        loserLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.BLACK);
        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(20));
        textPanel.add(winnerLabel);
        if (!losingTeam.equals("")) {
            textPanel.add(Box.createVerticalStrut(10));
            textPanel.add(loserLabel);
        }

        add(textPanel, BorderLayout.CENTER);
    }
}