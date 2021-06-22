package red.beard;

import red.beard.helpers.Help;
import red.beard.models.User;

import javax.swing.*;

public class Register extends JFrame {
    private JPanel rootPanel, Header, Body, Footer;
    private JTextField txtFirstName, txtLastName, txtEmail;
    private JRadioButton maleRadioButton, femaleRadioButton;
    private JButton registerButton;
    private JPasswordField pwdPassword, pwdPasswordConfirm;
    private JButton loginButton, btnXD;
    private String errorMsg = "Something went wrong.";
    String firstName, lastName, gender, email, password, passwordConfirm;

    public Register() {
        add(rootPanel);
        setTitle("Registration Form");

        registerButton.addActionListener(e -> {
            setFormValues();

            if(isValidForm()) {
                User newUser = new User(firstName, lastName, gender, email, password);

                try {
                    newUser.create();

                    JOptionPane.showMessageDialog(this, "New account created successfully! Proceed to login.");
                    Help.openFrame(new Login(), this);
                } catch(Exception err) {
                    JOptionPane.showMessageDialog(null, err.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, errorMsg);
            }
        });

        loginButton.addActionListener(e -> Help.openFrame(new Login(), this));
        btnXD.addActionListener(e -> System.exit(0));
    }

    private boolean isValidForm() {
        if(firstName.isEmpty() || lastName.isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() || passwordConfirm.trim().isEmpty()) {
            this.errorMsg = "Please fill in all required text fields!";
            return false;
        }

        if(!maleRadioButton.isSelected() && !femaleRadioButton.isSelected()) {
            this.errorMsg = "Please choose a gender.";
            return false;
        }

        if(!Help.emailIsValid(email)) {
            this.errorMsg = "Invalid email!";
            return false;
        }

        if(Help.emailExists(email)) {
            this.errorMsg = "Email already in use.";
            return false;
        }

        if(!password.equals(passwordConfirm)) {
            this.errorMsg = "Passwords do not match";
            return false;
        }

        return true;
    }

    public void setFormValues() {
        this.firstName = txtFirstName.getText().trim();
        this.lastName = txtLastName.getText().trim();
        this.email = txtEmail.getText();
        this.gender = (maleRadioButton.isSelected()) ? maleRadioButton.getText() : femaleRadioButton.getText();
        this.password = String.valueOf(pwdPassword.getPassword()).trim();
        this.passwordConfirm = String.valueOf(pwdPasswordConfirm.getPassword()).trim();
    }
}
