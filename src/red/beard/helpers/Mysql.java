package red.beard.helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mysql {
    private static final int PORT = 3306;
    private static final String HOST = "localhost";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "M1gu3l.";
    private static final String DATABASE = "rm_bank";
    private static Connection connection;

    public static Connection connectDb() {
        try {
            connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%d/%s", HOST, PORT, DATABASE), USERNAME, PASSWORD);
        } catch (SQLException ex) {
            Logger.getLogger(Mysql.class.getName()).log(Level.SEVERE, null, ex);
        }

        return connection;
    }
}
