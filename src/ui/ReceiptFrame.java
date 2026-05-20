package ui;

import model.OrderItem;
import logic.Calculator;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ReceiptFrame extends JFrame {
    private JFrame parent;
    private ArrayList<OrderItem> orders;
    private Calculator.Result result;
    private String receiptText;

    public ReceiptFrame(JFrame parent, ArrayList<OrderItem> orders, Calculator.Result result) {
        this.parent = parent;
        this.orders = orders;
        this.result = result;

        setTitle("Struk Pembayaran - Nikmat Lezat");
        setSize(490, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8,8));

        JTextArea ta = new JTextArea();
        ta.setEditable(false);
        ta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        receiptText = buildReceiptText();
        ta.setText(receiptText);
        add(new JScrollPane(ta), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("Simpan ke File");
        JButton btnBack = new JButton("Kembali ke Menu Utama");
        bottom.add(btnSave);
        bottom.add(btnBack);
        add(bottom, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> {
            this.setVisible(false);
            parent.setVisible(true);
            parent.dispose();
            MainMenuFrame mm = new MainMenuFrame();
            mm.setLocationRelativeTo(null);
            mm.setVisible(true);
        });

        btnSave.addActionListener(e -> {
            saveReceiptToFile(receiptText);
        });
    }

    private String buildReceiptText() {
        StringBuilder sb = new StringBuilder();
        sb.append("                             RESTORAN\n");
        sb.append("                           NIKMAT LEZAT\n");
        sb.append("                     Alamat: Denpasar - Bali\n");
        sb.append("---------------------------------------------------------------\n");
        sb.append(String.format("%-26s %4s %15s %15s\n","Item","Qty","Harga","Total"));
        sb.append("---------------------------------------------------------------\n");
        for (OrderItem oi : orders) {
            String name = oi.getMenu().getName();
            sb.append(String.format("%-26s %4d %15s %15s\n",
                    truncate(name,20),
                    oi.getQty(),
                    format(oi.getMenu().getPrice()),
                    format(oi.getTotal())
            ));
        }
        sb.append("---------------------------------------------------------------\n");
        sb.append(String.format("%-37s %25s\n","Subtotal", format(result.subtotal)));
        if (result.subtotal > 100000) {
            sb.append(String.format("%-37s %15s\n","Diskon 10% (setiap pembelian diatas RP 100.000)", format(result.discount)));
            sb.append(String.format("%-37s %25s\n","Subtotal setelah Diskon:", format(result.afterDiscount)));
        }
        sb.append(String.format("%-37s %25s\n","Pajak (10%)", format(result.tax)));
        sb.append(String.format("%-37s %25s\n","Biaya Pelayanan", format(result.service)));
        sb.append("---------------------------------------------------------------\n");
        sb.append(String.format("%-37s %25s\n","TOTAL BAYAR", format(result.total)));
        sb.append("\n                 Terima kasih atas kunjungan Anda\n");
        if (result.subtotal > 50000) {
            sb.append("                      !!! S E L A M A T !!!\n");
            sb.append("Anda mendapatkan penawaran Beli 1 Gratis 1 pada kategori Minuman\n");
            sb.append("             Silahkan tunjukkan struk ini kepada kasir\n");
        }
        return sb.toString();
    }

    private String format(int v) {
        return String.format("Rp %,d", v).replace(',', '.');
    }

    private String truncate(String s, int len) {
        if (s.length() <= len) return s;
        return s.substring(0, len-3) + "...";
    }

    private void saveReceiptToFile(String text) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Struk sebagai");
        chooser.setSelectedFile(new File(defaultFileName()));
        int userSelection = chooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = chooser.getSelectedFile();
            // Pastikan ekstensi .txt
            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".txt");
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                writer.write(text);
                writer.flush();
                JOptionPane.showMessageDialog(this, "Struk berhasil disimpan:\n" + fileToSave.getAbsolutePath(), "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan struk:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String defaultFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String ts = sdf.format(new Date());
        return "struk_" + ts + ".txt";
    }
}
