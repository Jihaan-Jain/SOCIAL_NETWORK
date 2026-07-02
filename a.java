import javax.swing.*;
import java.awt.*;

public class a {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Login Form"
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE
            frame.setSize(400, 220);
            frame.setLocationRelativeTo(null);

            JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel userLabel = new JLabel("Username:");
            JTextField userField = new JTextField();
            JLabel passLabel = new JLabel("Password:");
            JPasswordField passField = new JPasswordField();
            JButton loginButton = new JButton("Login");

            panel.add(userLabel);
            panel.add(userField);
            panel.add(passLabel);
            panel.add(passField);
            panel.add(new JLabel(""));
            panel.add(loginButton);

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}