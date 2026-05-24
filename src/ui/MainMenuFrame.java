package ui;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    public MainMenuFrame() {
        setTitle("Aplikasi Restoran v1.0");
        setSize(420, 260);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8,8));

        JLabel header = new JLabel("<html><center><h2>NIKMAT LEZAT</h2><small>Alamat: Denpasar - Bali</small></center></html>", SwingConstants.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel center = new JPanel();
        center.setLayout(new GridLayout(3,1,8,8));
        JButton btnOrder = new JButton("Pemesanan");
        JButton btnManage = new JButton("Manajemen Menu");
        JButton btnExit = new JButton("Keluar");

        center.add(btnOrder);
        center.add(btnManage);
        center.add(btnExit);
        add(center, BorderLayout.CENTER);

        btnOrder.addActionListener(e -> {
            OrderFrame of = new OrderFrame(this);
            of.setLocationRelativeTo(this);
            of.setVisible(true);
            this.setVisible(false);
        });

        btnManage.addActionListener(e -> {
            JPasswordField pf = new JPasswordField();
            int ok = JOptionPane.showConfirmDialog(this, pf, "Masukkan Password untuk Manajemen Menu (1234):", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (ok == JOptionPane.OK_OPTION) {
                String pass = new String(pf.getPassword());
                if ("1234".equals(pass)) {
                    ManageMenuFrame mm = new ManageMenuFrame(this);
                    mm.setLocationRelativeTo(this);
                    mm.setVisible(true);
                    this.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(this, "Password salah.");
                }
            }
        });

        btnExit.addActionListener(e -> System.exit(0));
    }
}
