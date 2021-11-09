package red.beard;

import red.beard.helpers.Help;
import red.beard.models.Account;
import red.beard.models.User;

import javax.swing.*;
import java.time.LocalTime;

public class Index extends JFrame {
    private JPanel rootPanel;
    private JLabel lblGreet, lblAccNo;
    private JButton depositButton, withdrawButton, transferButton, checkBalanceButton, signOutButton, btnUpdateProfile,
            btnChangePassword, btnRecentTransact;
    private JTextField txtFirstName, txtLastName, txtGender, txtPhone, txtEmail;
    private JTabbedPane tabbedPane1;
    User sessionUser;

    String errorMsg, firstName, lastName, phone, email;

    LocalTime currentTime = LocalTime.now();
    LocalTime morningStart = LocalTime.of(0, 1);
    LocalTime afternoonStart = LocalTime.of(12, 1);
    LocalTime eveningStart = LocalTime.of(18, 1);

    public Index(User user) {
        this.sessionUser = user;

        add(rootPanel);
        lblGreet.setText(getWelcomeMessage());
        lblAccNo.setText("Account number: " + this.sessionUser.getAccNo());

        txtFirstName.setText(sessionUser.getFirstName());
        txtLastName.setText(sessionUser.getLastName());
        txtGender.setText(sessionUser.getGender());
        txtEmail.setText(sessionUser.getEmail());
        txtPhone.setText(0 + String.valueOf(sessionUser.getPhone()));

        actionListeners();
    }

    private void actionListeners() {
        depositButton.addActionListener(e -> {
            try {
                String amount = JOptionPane.showInputDialog(this, "Enter amount to deposit", "Amount", JOptionPane.INFORMATION_MESSAGE);

                if(amount != null) {
                    Account acc = new Account(sessionUser.getId()).deposit(Float.parseFloat(amount));
                    JOptionPane.showMessageDialog(this, "Success! new account balance is: " + acc.checkBalance());
                }
            } catch (NumberFormatException error) {
                JOptionPane.showMessageDialog(this, "Please enter a number");
            }
        });

        withdrawButton.addActionListener(e -> {
            try {
                String amount = JOptionPane.showInputDialog(this, "Enter amount to withdraw", "Amount", JOptionPane.INFORMATION_MESSAGE);

                if(amount != null) {
                    Account acc = new Account(sessionUser.getId());

                    if (acc.checkBalance() > Float.parseFloat(amount)) {
                        acc.withdraw(Float.parseFloat(amount));
                        JOptionPane.showMessageDialog(this, "Success! new account balance is: " + acc.checkBalance());
                    } else {
                        JOptionPane.showMessageDialog(this, "Your account balance is insufficient.", "Oops!", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } catch (NumberFormatException error) {
                JOptionPane.showMessageDialog(this, "Please enter a number");
            }
        });

        transferButton.addActionListener(e -> {
            try {
                String accountNumber = JOptionPane.showInputDialog(
                        this, "Enter account number to send to", "Account Number", JOptionPane.INFORMATION_MESSAGE);

                if(accountNumber != null) {
                    if (!Help.isNumeric(accountNumber)) {
                        throw new Exception("Account number Must be numeric.");
                    }

                    float amount = Float.parseFloat(JOptionPane.showInputDialog(this, "Enter amount to send", "Amount", JOptionPane.INFORMATION_MESSAGE));

                    Account acc = new Account(sessionUser.getId());

                    if (acc.sendMoney(Integer.parseInt(accountNumber), amount, this)) {
                        JOptionPane.showMessageDialog(this,
                                "You have sent " + amount + " to account number " + accountNumber + ". New account balance is: " + acc.checkBalance(),
                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (NumberFormatException error) {
                JOptionPane.showMessageDialog(this, "Amount must be numeric.");
            } catch(NullPointerException err) {
                System.out.println(err.getMessage());
            } catch (Exception err) {
                System.out.println(err.getMessage());
                JOptionPane.showMessageDialog(this, err.getMessage(), "Error!", JOptionPane.WARNING_MESSAGE);
            }
        });

        checkBalanceButton.addActionListener(e -> {
            try {
                float balance = new Account(sessionUser.getId()).checkBalance();

                JOptionPane.showMessageDialog(this, "Current account balance is: " + balance);
            } catch (NumberFormatException error) {
                JOptionPane.showMessageDialog(this, "Please enter a number");
            }
        });

        btnChangePassword.addActionListener(e -> Help.openFrame(new Password(sessionUser)));

        btnUpdateProfile.addActionListener(e -> {
            if (profileFormIsValid()) {
                if (sessionUser.updateProfile(Integer.parseInt(phone), firstName, lastName, email, this)) {
                    JOptionPane.showMessageDialog(this, "Profile updated!");
                } else {
                    JOptionPane.showMessageDialog(this, "Unable to update profile!", "Oops!", JOptionPane.WARNING_MESSAGE);
                }
            } else if (errorMsg != null) {
                JOptionPane.showMessageDialog(this, errorMsg);
            }
        });

        btnRecentTransact.addActionListener(e -> Help.openFrame(new Log(sessionUser)));

        signOutButton.addActionListener(e -> Help.openFrame(new Login(), this));
    }

    private boolean profileFormIsValid() {
        setFormValues();

        if (firstName.isEmpty() || lastName.isEmpty() || email.trim().isEmpty()) {
            this.errorMsg = "Please fill in all required text fields!";
            return false;
        }

        if (!Help.phoneIsValid(phone, sessionUser.getId(), this)) {
            return false;
        }

        if (!Help.emailIsValid(email)) {
            this.errorMsg = "Invalid email!";
            return false;
        }

        if (Help.emailExists(email, sessionUser.getId())) {
            this.errorMsg = "Email already in use.";
            return false;
        }

        return true;
    }

    private String getWelcomeMessage() {
        StringBuilder message = new StringBuilder();

        message.append("Good ").append(timePeriod()).append(" ");

        if (sessionUser.getGender() == null) {
            message.append("!");
        } else {
            //message.append(salutation());
            message.append(Help.capitalize(sessionUser.getLastName()));
        }

        return String.valueOf(message);
    }

    private String salutation() {
        String salute = "";
        if (sessionUser.getGender().equals("Female")) {
            salute = "Mrs. ";
        } else if (sessionUser.getGender().equals("Male")) {
            salute = "Mr. ";
        }

        return salute;
    }

    private String timePeriod() {
        if (currentTime.isAfter(morningStart) && currentTime.isBefore(afternoonStart)) {
            return "Morning";
        } else if (currentTime.isAfter(afternoonStart) && currentTime.isBefore(eveningStart)) {
            return "Afternoon";
        }

        return "Evening";
    }

    public void setFormValues() {
        this.firstName = txtFirstName.getText().trim();
        this.lastName = txtLastName.getText().trim();
        this.phone = txtPhone.getText();
        this.email = txtEmail.getText();
    }
}
