package ui;

import javax.swing.SwingUtilities;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenuFrame main = new MainMenuFrame();
            main.setLocationRelativeTo(null);
            main.setVisible(true);
        });
    }
}
