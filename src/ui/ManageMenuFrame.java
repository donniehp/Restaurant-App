package ui;

import data.MenuData;
import model.MenuItem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ManageMenuFrame extends JFrame {
    private JFrame parent;
    private JTable table;
    private DefaultTableModel model;

    public ManageMenuFrame(JFrame parent) {
        this.parent = parent;
        setTitle("Manajemen Menu - Nikmat Lezat");
        setSize(700, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8,8));

        JLabel header = new JLabel("<html><h2>Manajemen Menu</h2></html>", SwingConstants.CENTER);
        add(header, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"No","Nama","Kategori","Harga"},0) {
            public boolean isCellEditable(int r,int c){return false;}
        };
        table = new JTable(model);
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,8));
        JButton btnAdd = new JButton("Tambah");
        JButton btnEdit = new JButton("Ubah Harga");
        JButton btnDelete = new JButton("Hapus");
        JButton btnBack = new JButton("Kembali");
        buttons.add(btnAdd);
        buttons.add(btnEdit);
        buttons.add(btnDelete);
        buttons.add(btnBack);
        add(buttons, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField price = new JTextField();
            String[] categories = {"Makanan","Minuman"};
            JComboBox<String> cb = new JComboBox<>(categories);
            Object[] fields = {
                    "Nama:", name,
                    "Harga (angka):", price,
                    "Kategori:", cb
            };
            int ok = JOptionPane.showConfirmDialog(this, fields, "Tambah Menu", JOptionPane.OK_CANCEL_OPTION);
            if (ok == JOptionPane.OK_OPTION) {
                try {
                    String n = name.getText().trim();
                    int p = Integer.parseInt(price.getText().trim());
                    String cat = (String) cb.getSelectedItem();
                    MenuData.addMenu(new MenuItem(n, p, cat));
                    refreshTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Input tidak valid.");
                }
            }
        });

        btnEdit.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel == -1) { JOptionPane.showMessageDialog(this, "Pilih menu untuk diubah."); return; }
            int idx = sel;
            MenuItem m = MenuData.getMenuList().get(idx);
            String newPrice = JOptionPane.showInputDialog(this, "Harga baru untuk " + m.getName() + " (sekarang Rp " + m.getPrice() + "):");
            if (newPrice == null) return;
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ubah harga?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    int p = Integer.parseInt(newPrice.trim());
                    m.setPrice(p);
                    MenuData.updateMenu(idx, m);
                    refreshTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Input harga tidak valid.");
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int sel = table.getSelectedRow();
            if (sel == -1) { JOptionPane.showMessageDialog(this, "Pilih menu untuk dihapus."); return; }
            int idx = sel;
            MenuItem m = MenuData.getMenuList().get(idx);
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus " + m.getName() + " ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                MenuData.removeMenu(idx);
                refreshTable();
            }
        });

        btnBack.addActionListener(e -> {
            this.setVisible(false);
            parent.setVisible(true);
        });
    }

    private void refreshTable() {
        model.setRowCount(0);
        int i = 0;
        for (MenuItem m : MenuData.getMenuList()) {
            model.addRow(new Object[]{i, m.getName(), m.getCategory(), m.getPrice()});
            i++;
        }
    }
}
