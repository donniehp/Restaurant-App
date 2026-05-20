package ui;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {
    public MainMenuFrame() {
        setTitle("Nikmat Lezat - Menu Utama");
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
            ManageMenuFrame mm = new ManageMenuFrame(this);
            mm.setLocationRelativeTo(this);
            mm.setVisible(true);
            this.setVisible(false);
        });

        btnExit.addActionListener(e -> System.exit(0));
    }
}
