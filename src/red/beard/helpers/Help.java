package red.beard.helpers;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Help {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_PATTERN = "((^0[17]+)|(^[17]+)).*";

    public static String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase();
    }

    public static boolean emailIsValid(String email) {
        return email.matches(EMAIL_PATTERN);
    }

    public static boolean emailExists(String email) {
        Connection connection = Mysql.connectDb();
        String query = "SELECT email FROM users where email = '" + email + "' LIMIT 1";

        try {
            assert false;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        return false;
    }

    public static boolean emailExists(String email, int ignoreUser) {
        Connection connection = Mysql.connectDb();
        String query = "SELECT email FROM users WHERE id != " + ignoreUser + " email = '" + email + "' LIMIT 1";

        try {
            assert false;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        return false;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean phoneExists(int phone, int id) {
        Connection connection = Mysql.connectDb();

        String query = id == 0 ? "SELECT phone FROM users where phone = '" + phone + "' LIMIT 1" :
                "SELECT id, phone FROM users where phone = '" + phone + "' AND id != " + id + " LIMIT 1";

        try {
            assert false;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }

        return false;
    }


    public static boolean phoneIsValid(String phone, int ignoreUser, JFrame parentComponent) {
        if(phone != null && !phone.trim().isEmpty()) {
            if(!isNumeric(phone)) {
                JOptionPane.showMessageDialog(parentComponent, "Phone number has to be an integer!", "Oops!", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if(!phone.matches(PHONE_PATTERN)) {
                JOptionPane.showMessageDialog(parentComponent, "Invalid phone number", "Oops!", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if(phone.length() != 10) {
                JOptionPane.showMessageDialog(parentComponent, "Phone number must be 10 digits.", "Oops!", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if(phoneExists(Integer.parseInt(phone), ignoreUser)) {
                JOptionPane.showMessageDialog(parentComponent, "Phone number already in use.", "Oops!", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            return true;
        }

        return false;
    }

    public static void openFrame(JFrame frame) {
        frame.setSize(500, 400);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(true);
        frame.setVisible(true);
    }

    public static void openFrame(JFrame frame, JFrame closeFrame) {
        openFrame(frame);
        closeFrame.setVisible(false);
    }
}
