import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {

    public LoginPanel(LoginMethod loginMethod) {
        JTextField usernameField = new JTextField(10);
        JTextField passwordField = new JTextField(10);
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> loginMethod.attemptLogin(usernameField.getText(), passwordField.getText()));

        setLayout(new GridLayout(3, 2));
        add(new JLabel("Username"));
        add(usernameField);
        add(new JLabel("Password"));
        add(passwordField);
        add(new JLabel(""));
        add(loginButton);
    }

    public interface LoginMethod {
        void attemptLogin(String username, String password);
    }

}
