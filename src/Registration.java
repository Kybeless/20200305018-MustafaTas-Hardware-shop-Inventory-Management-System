import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Registration extends JFrame {

    private JTextField textField1;
    private JPasswordField passwordField1;
    private JButton OKButton;
    private JPanel panelMain;
    private JButton backButton;

    private Connection connection;
    private Statement statement;


    public Registration() {

        setTitle("Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setContentPane(panelMain);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textField1Text = textField1.getText();
                char[] passwordChars = passwordField1.getPassword();
                String password = new String(passwordChars);

                if (!textField1Text.isEmpty()) {
                    try {
                        String name = textField1Text;
                        if (authenticate(name, password)) {
                            RegistrationDataBase(name, password); // RegistrationDataBase() metodu çağrıldı
                            textField1.setText("");
                            passwordField1.setText("");
                            JOptionPane.showMessageDialog(null, "Veriler başarıyla kaydedildi.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Kimlik doğrulama başarısız.");
                            return; // Hata durumunda geri dön
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Hata oluştu: " + ex.getMessage());
                        return; // Hata durumunda geri dön
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "İsim alanı boş olamaz.");
                    return; // Hata durumunda geri dön
                }
                // Devam eden işlemler
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

    private void RegistrationDataBase(String name, String password) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/20200305018", "root", "");
            statement = connection.createStatement();

            String query = "INSERT INTO login (Name, Password) VALUES ('" + name + "', '" + password + "')";
            statement.executeUpdate(query);

            statement.close();
            connection.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean authenticate(String name, String password) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/20200305018", "root", "");
            statement = connection.createStatement();

            String query = "SELECT * FROM login WHERE Name='" + name + "'";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                JOptionPane.showMessageDialog(null, "Bu isim zaten kayıtlı.");
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (statement != null)
                    statement.close();

                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Registration registration = new Registration();
                registration.setVisible(true);
            }
        });
    }
}
