package red.beard.models;

import red.beard.helpers.Mysql;

import java.sql.*;

public class Account {
    private int id, user_id;
    private float balance;

    public Account(int user_id) {
        this.user_id = user_id;
    }

    public Account deposit(float amount) {
        String sql = "UPDATE account SET balance = ?, updated_at = NOW() WHERE user_id = ?";

        try (Connection link = Mysql.connectDb()) {
            PreparedStatement statement = link.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setFloat(1, (amount + checkBalance()));
            statement.setInt(2, this.user_id);

            statement.executeUpdate();

            return this;
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.getCause().printStackTrace();
        }

        return this;
    }

    public Account withdraw(float amount) {
        String sql = "UPDATE account SET balance = ?, updated_at = NOW() WHERE user_id = ?";

        try (Connection link = Mysql.connectDb()) {
            PreparedStatement statement = link.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setFloat(1, (checkBalance() - amount));
            statement.setInt(2, this.user_id);

            statement.executeUpdate();

            return this;
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.getCause().printStackTrace();
        }

        return this;
    }

    public float checkBalance() {
        String sql = "SELECT balance FROM account WHERE user_id = " + this.user_id + " LIMIT 1";

        try (Connection link = Mysql.connectDb()) {
            Statement statement = link.createStatement();
            ResultSet result = statement.executeQuery(sql);

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

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
}
