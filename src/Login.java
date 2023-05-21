import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {
    private JTextField usernameField;

    private JPasswordField passwordField;

    private JButton btnLogin;

    private JPanel panel1;

    private JButton btnRegistration;

    private Connection connection;
    private Statement statement;

    public Login() {
        setTitle("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        setContentPane(panel1);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordField.getPassword());

                if (authenticate(username, password)) {
                    JOptionPane.showMessageDialog(Login.this, "Giriş başarılı!");
                    Inventory inventory = new Inventory();
                    inventory.setVisible(true);
                    setVisible(false);

                } else {
                    JOptionPane.showMessageDialog(Login.this, "Geçersiz kullanıcı adı veya parola");
                }
            }
        });

        btnRegistration.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Registration registration = new Registration();
                registration.setVisible(true);
                setVisible(false);
            }
        });
    }

    private boolean authenticate(String username, String password) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/20200305018", "root", "");
            statement = connection.createStatement();

            String query = "SELECT * FROM login WHERE Name='" + username + "' AND Password='" + password + "'";
            ResultSet resultSet = statement.executeQuery(query);

            return resultSet.next();
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
                Login loginForm = new Login();
                loginForm.setVisible(true);
            }
        });
    }
}
