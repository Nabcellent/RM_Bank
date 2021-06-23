package red.beard;

import red.beard.helpers.Mysql;
import red.beard.models.Account;
import red.beard.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Log extends JFrame {
    private JPanel rootPanel;
    private JTable tblLogs;
    private JPanel headerPanel;
    private JButton btnBackToProfile;
    private JPanel bodyPanel;
    private JScrollPane spScroll;
    User sessionUser;

    public Log(User user) {
        this.sessionUser = user;

        add(rootPanel);

        tblLogs.setModel(new Account(sessionUser.getId()).getLogs());

        btnBackToProfile.addActionListener(e -> {
            this.setVisible(false);
        });
    }
}
