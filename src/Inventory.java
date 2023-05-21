import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Inventory extends JFrame {

    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JButton addButton;
    private JButton deleteButton;
    private JPanel panelMain;
    private JButton backButton;
    private JTable table1;

    private Connection connection;

    public Inventory() {
        setTitle("Inventory");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setContentPane(panelMain);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        connectToDatabase(); // Bağlantıyı oluştur
        loadStockDataFromDatabase(); // Tabloyu yükle

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productID = textField1.getText();
                String description = textField2.getText();
                String vendor = textField3.getText();
                String stockLocation = textField4.getText();
                try {
                    // Veritabanına veri ekleme işlemleri
                    String query = "INSERT INTO inventory (`Product ID`, `Description`, `Vendor`, `Stock Location`) VALUES (?, ?, ?, ?)";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, Integer.parseInt(productID));
                    statement.setString(2, description);
                    statement.setString(3, vendor);
                    statement.setString(4, stockLocation);
                    statement.executeUpdate();

                    // Verilerin yüklenmesi
                    loadStockDataFromDatabase();

                    // Alanların temizlenmesi
                    textField1.setText("");
                    textField2.setText("");
                    textField3.setText("");
                    textField4.setText("");

                    JOptionPane.showMessageDialog(null, "Data saved successfully.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error occurred while saving data.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Silme düğmesi eylemi burada gerçekleştirilecek
                int selectedRow = table1.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");
                    return;
                }
                int id = Integer.parseInt(table1.getValueAt(selectedRow, 0).toString());
                try {
                    String query = "DELETE FROM inventory WHERE `Product ID` = ?";
                    PreparedStatement statement = connection.prepareStatement(query);
                    statement.setInt(1, id);
                    statement.executeUpdate();
                    loadStockDataFromDatabase();
                    textField1.setText("");
                    textField2.setText("");
                    textField3.setText("");
                    textField4.setText("");
                    JOptionPane.showMessageDialog(null, "Data deleted successfully.");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login loginForm = new Login();
                loginForm.setVisible(true);
                dispose();
            }
        });
    }

    private void loadStockDataFromDatabase() {
        // Veritabanından stok verilerini yükleme işlemleri burada yapılır
        try {
            String query = "SELECT * FROM inventory";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            String[] columnNames = {"Product ID", "Description", "Vendor", "Stock Location"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            while (resultSet.next()) {
                int productID = resultSet.getInt("Product ID");
                String description = resultSet.getString("Description");
                String vendor = resultSet.getString("Vendor");
                String stockLocation = resultSet.getString("Stock Location");
                Object[] rowData = {productID, description, vendor, stockLocation};
                model.addRow(rowData);
            }
            table1.setModel(model);

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/20200305018", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to connect to the database.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Inventory inventory = new Inventory();
                inventory.setTitle("Inventory");
                inventory.setVisible(true);
            }
        });
    }
}
