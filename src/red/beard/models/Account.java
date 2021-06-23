package red.beard.models;

import red.beard.helpers.Mysql;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.DateFormat;
import java.util.Arrays;

public class Account {
    private int accNo;
    private final int userId;
    private float balance;
    private String sql;

    public Account(int userId) {
        this.userId = userId;
    }

    public Account deposit(float amount) {
        String sql = "UPDATE account SET balance = ?, updated_at = NOW() WHERE user_id = ?";

        try (Connection link = Mysql.connectDb()) {
            PreparedStatement statement = link.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setFloat(1, (amount + checkBalance()));
            statement.setInt(2, this.userId);

            statement.executeUpdate();

            this.updateLogs(this.getAccNo(), amount,"Deposited");

            return this;
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.getCause().printStackTrace();
        }

        return this;
    }

    public void withdraw(float amount) {
        String sql = "UPDATE account SET balance = ?, updated_at = NOW() WHERE user_id = ?";

        try (Connection link = Mysql.connectDb()) {
            PreparedStatement statement = link.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setFloat(1, (checkBalance() - amount));
            statement.setInt(2, this.userId);

            statement.executeUpdate();

            this.updateLogs(this.getAccNo(), amount, "Withdrew");
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.getCause().printStackTrace();
        }

    }

    public float checkBalance() {
        String sql = "SELECT balance FROM account WHERE user_id = " + this.userId + " LIMIT 1";

        try (Connection link = Mysql.connectDb()) {
            ResultSet result = link.createStatement().executeQuery(sql);

            if (result.next()) {
                this.balance = result.getInt("balance");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.getCause().printStackTrace();
        }

        return this.balance;
    }

    public boolean sendMoney(int accountNumber, float amount, JFrame parentComponent) {
        if(this.checkBalance() > amount) {

            String sql = "UPDATE account SET balance = ?, updated_at = NOW() WHERE id = ?";

            try (Connection link = Mysql.connectDb()) {
                PreparedStatement statement = link.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                statement.setFloat(1, (new Account(accountNumber).checkBalance() + amount));
                statement.setInt(2, accountNumber);

                if(statement.executeUpdate() == 1) {
                    sql = "UPDATE account SET balance = " + (checkBalance() - amount) + ", updated_at = NOW() WHERE user_id = " + this.userId;
                    link.createStatement().executeUpdate(sql);

                    this.updateLogs(this.getAccNo(), amount, "Transferred");
                    this.updateLogs(accountNumber, amount, "Received");

                    return true;
                } else {
                    JOptionPane.showMessageDialog(parentComponent, "There is no user with the account number you provided.");
                }
            } catch (SQLException se) {
                se.printStackTrace();
            } catch (Exception e) {
                e.getCause().printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(parentComponent, "Your account balance is insufficient to make this transaction.");
        }

        return false;
    }

    public int getAccNo() {
        String sql = "SELECT id FROM account WHERE user_id = " + this.userId + " LIMIT 1";

        try (Connection link = Mysql.connectDb()) {
            ResultSet result = link.createStatement().executeQuery(sql);

            if (result.next()) {
                this.accNo = result.getInt("id");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.getCause().printStackTrace();
        }

        return accNo;
    }

    public void updateLogs(int accountNo, float amount, String status) {
        try (Connection link = Mysql.connectDb()) {
            PreparedStatement statement = link.prepareStatement("INSERT INTO logs (account_id, amount, status) VALUES (?, ?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, accountNo);
            statement.setFloat(2, amount);
            statement.setString(3, status);

            statement.executeUpdate();

            System.out.println("!Logs updated: " + status);
        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println(se.getMessage());
        } catch (Exception e) {
            e.getCause().printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public DefaultTableModel getLogs() {
        DefaultTableModel model = new DefaultTableModel();

        model.addColumn("Action");
        model.addColumn("Amount");
        model.addColumn("Date");

        DateFormat Date = DateFormat.getDateInstance();

        try(Connection link = Mysql.connectDb();
            Statement stmt = link.createStatement();
            ResultSet results = stmt.executeQuery("SELECT * FROM logs WHERE account_id = " + this.getAccNo());
        ) {
            while(results.next()) {
                model.addRow(new Object[] {
                        results.getString("status"),
                        results.getString("amount"),
                        Date.format(results.getDate("updated_at")),
                });
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return model;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
