package red.beard;

import red.beard.models.User;

import javax.swing.*;

public class Password extends JFrame {
    private JPanel rootPanel, headerPanel, bodyPanel, footerPanel;
    private JButton btnUpdatePassword, btnBackToProfile;
    private JPasswordField pwdNewPass, pwdConfirmNewPass, pwdCurrentPass;
    private String errorMsg, currentPass, newPass, newPassConfirmation;
    User sessionUser;

    public Password(User user) {
        this.sessionUser = user;

        add(rootPanel);
        setTitle("Update Password");

        btnBackToProfile.addActionListener(e -> this.setVisible(false));
        btnUpdatePassword.addActionListener(e -> {
            setFormValues();

            if(passwordsAreValid()) {
                if(sessionUser.changePassword(newPass, this)) {
                    JOptionPane.showMessageDialog(this, "Password updated!");
                    this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Unable to change password!", "Sorry!", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, errorMsg, "Oops!", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public boolean passwordsAreValid() {
        if(currentPass.isEmpty() || newPass.isEmpty() || newPassConfirmation.isEmpty()) {
            this.errorMsg = "Please fill in all required fields.";
            return false;
        }

        if(!sessionUser.checkCurrentPassword(currentPass)) {
            this.errorMsg = "Current password is incorrect.";
            return false;
        }

        if(!newPass.equals(newPassConfirmation)) {
            this.errorMsg = "Your confirmation password doesn't match the new password.";
            return false;
        }

        return true;
    }

    public void setFormValues() {
        this.currentPass = String.valueOf(pwdCurrentPass.getPassword()).trim();
        this.newPass = String.valueOf(pwdNewPass.getPassword()).trim();
        this.newPassConfirmation = String.valueOf(pwdConfirmNewPass.getPassword()).trim();
    }
}
