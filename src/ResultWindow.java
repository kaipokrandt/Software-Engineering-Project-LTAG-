import javax.swing.*;
import java.awt.*;

public class ResultWindow extends JFrame {

    public ResultWindow(int redScore, int greenScore) {
        setTitle("Game Results");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

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
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));

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

        add(resultLabel, BorderLayout.CENTER);
    }

    private String toHex(Color color) {
        return String.format("#%02x%02x%02x",
                color.getRed(), color.getGreen(), color.getBlue());
    }
}