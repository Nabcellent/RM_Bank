package red.beard;

import red.beard.helpers.Help;
import red.beard.helpers.Mysql;
import red.beard.models.User;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Login extends JFrame {
    private JPanel rootPanel;
    private JTextField txtEmail;
    private JButton signInButton;
    private JPasswordField pwdPassword;
    private JButton signUpButton;
    private JButton btnXD;
    private JPanel headerPanel;
    private String errorMsg = "Something went wrong.";
    private String email, password;
    User sessionUser;

    public Login() {
        add(rootPanel);
        setTitle("Registration Form");

        signInButton.addActionListener(e -> {
            getFormValues();

            if(isValidForm()) {
                if(authenticateLogin()) {
                    Help.openFrame(new Index(sessionUser), this);
                } else {
                    JOptionPane.showMessageDialog(Login.this, this.errorMsg);
                }
            } else {
                JOptionPane.showMessageDialog(Login.this, this.errorMsg);
            }
        });
        signUpButton.addActionListener(e -> Help.openFrame(new Register(), this));
        btnXD.addActionListener(e -> System.exit(0));
    }

    private boolean isValidForm() {
        if(email.isEmpty() || password.isEmpty()) {
            this.errorMsg = "Please fill in all fields!";
            return false;
        }

        return true;
    }

    private boolean authenticateLogin() {
        Connection link = Mysql.connectDb();

        String verifyLogin = "SELECT * FROM users WHERE email = '" + email + "' AND password = '" + password + "' LIMIT 1";

        try {
            Statement statement = link.createStatement();
            ResultSet qryResult = statement.executeQuery(verifyLogin);

            if (qryResult.next()) {
                sessionUser = new User(
                        qryResult.getInt("id"),
                        qryResult.getString("first_name"),
                        qryResult.getString("last_name"),
                        qryResult.getString("gender"),
                        qryResult.getString("email"),
                        qryResult.getInt("phone")
                );
                return true;
            } else {
                throw new Exception("Invalid Credentials");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            errorMsg = e.getMessage();

            return false;
        }
    }

    public void getFormValues() {
        this.email = txtEmail.getText();
        this.password = String.valueOf(pwdPassword.getPassword());
    }
}
