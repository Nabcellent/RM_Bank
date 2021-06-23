package red.beard;

import red.beard.helpers.Help;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // write your code here
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");

        SwingUtilities.invokeLater(() -> {
            Help.openFrame(new Login());
        });
    }
}


/*
rootPanel - 1E1F31 - Dark blue
#B81283 - Magenta
#B2BED4 - Light Blue
*/