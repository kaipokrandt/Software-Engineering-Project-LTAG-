import java.awt.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;


class EntryScreen {

    database db = null;

    public void setVariable(database db){
        this.db = db;
    }
    public static void showPlayerEntryScreen() {
        JFrame frame = new JFrame("Player Entry");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500,500);
        frame.setLayout(new GridLayout(5,2));

        JTextField playerNameField = new JTextField();
        JTextField IdField = new JTextField();
        JButton addButton = new JButton("Add Player");

        frame.add(new JLabel("Enter Player Name: ", SwingConstants.CENTER));
        frame.add(playerNameField);
        frame.add(IdField);
        frame.add(addButton);

        DefaultTableModel model = new DefaultTableModel(new Object[]{"Name","Id"},0); 
        JTable table = new JTable(model);

//     addButton.addActionListener(new ActionListener() {
//         @Override
//         public void actionPerformed(Action e){
//             String playerName = playerNameField.getText();
//             if (!playerName.isEmpty()) {
//             db.addplayer(playerName, ID);
//             new udpBaseClient_2().sendEquipmentCode("Player Name: " + playerName);
//         }
//     }
// });

        frame.setVisible(true);
    }

}
