package red.beard.models;

import red.beard.helpers.Mysql;

import javax.swing.*;
import java.sql.*;

public class User {
    private int id, phone;
    private String firstName, lastName, gender, email, password;

    public User(String firstName, String lastName, String gender, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.password = password;
    }

    public User(int id, String firstName, String lastName, String gender, String email, int phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }

    public void create() {
        Connection link = Mysql.connectDb();

        String sql = "INSERT INTO users (first_name, last_name, gender, email, password, created_at) " +
                "VALUES (?,?,?,?,?, NOW())";

        try {
            PreparedStatement statement = link.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, this.firstName);
            statement.setString(2, this.lastName);
            statement.setString(3, this.gender);
            statement.setString(4, this.email);
            statement.setString(5, this.password);

            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();

            if (result.next()) {
                int userId = result.getInt(1);

                sql = "INSERT INTO account(user_id) VALUES (" + userId + ")";

                Statement accStatement = link.createStatement();
                accStatement.executeUpdate(sql);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.getCause().printStackTrace();
        }

    }

    public boolean updateProfile(int phone, String firstName, String lastName, String email, JFrame parentComponent) {
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, phone = ?, updated_at = NOW() WHERE id = ?";

        try (Connection link = Mysql.connectDb()) {
            PreparedStatement statement = link.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.setInt(4, phone);
            statement.setInt(5, this.id);

            return statement.executeUpdate() == 1;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(parentComponent, e.getMessage(), "Error!", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    public boolean changePassword(String newPass, JFrame parentComponent) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection link = Mysql.connectDb()) {
            PreparedStatement statement = link.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, newPass);
            statement.setInt(2, this.id);

            return statement.executeUpdate() == 1;
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(parentComponent, e.getMessage(), "Error!", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    public boolean checkCurrentPassword(String password) {
        String sql = "SELECT password FROM users WHERE id = " + this.id + " LIMIT 1";

        try (Connection link = Mysql.connectDb()) {
            Statement statement = link.createStatement();
            ResultSet result = statement.executeQuery(sql);

            if (result.next()) {
                return result.getString("password").equals(password);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.getCause().printStackTrace();
        }

        return false;
    }

    /*public User authenticate() {
        Connection link = Mysql.connectDb();

        String verifyLogin = "SELECT * FROM users WHERE email = '" + email + "' AND password = '" + password + "' LIMIT 1";

        try {
            Statement statement = link.createStatement();
            ResultSet qryResult = statement.executeQuery(verifyLogin);

            if (qryResult.next()) {
                return new User(
                        qryResult.getInt("id"),
                        qryResult.getString("first_name"),
                        qryResult.getString("last_name"),
                        qryResult.getString("gender"),
                        qryResult.getString("email"),
                        qryResult.getInt("phone")
                );
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Credentials");
                throw new Exception("Invalid Credentials");
            }
        } catch (Exception e) {
            e.getCause();
            e.printStackTrace();
        }

        return this;
    }*/

    public int getId() {
        return id;
    }

    public int getPhone() {
        return phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }
}
