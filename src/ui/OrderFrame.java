package ui;

import data.MenuData;
import model.MenuItem;
import model.OrderItem;
import logic.Calculator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class OrderFrame extends JFrame {
    private final JFrame parent;
    private final JTable menuTable;
    private final DefaultTableModel menuModel;
    private final JTable cartTable;
    private final DefaultTableModel cartModel;
    private final ArrayList<OrderItem> cart = new ArrayList<>();

    public OrderFrame(JFrame parent) {
        this.parent = parent;

        setTitle("Pemesanan - Nikmat Lezat");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Ukuran awal lebih besar, minimal dan pusat layar
        setSize(1200, 720);
        setMinimumSize(new Dimension(1000, 600));
        setLocationRelativeTo(null);

        // Header
        JLabel header = new JLabel("<html><center><h2>Pemesanan - Nikmat Lezat</h2><small>Alamat: Denpasar - Bali</small></center></html>", SwingConstants.CENTER);
        add(header, BorderLayout.NORTH);

        // Menu table (kiri)
        menuModel = new DefaultTableModel(new Object[]{"No", "Nama", "Kategori", "Harga"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        menuTable = new JTable(menuModel);
        menuTable.setFillsViewportHeight(true);
        refreshMenuTable();
        JScrollPane menuScroll = new JScrollPane(menuTable);
        menuScroll.setPreferredSize(new Dimension(520, 600));

        // Controls (tengah atas of right panel)
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblQty = new JLabel("Jumlah:");
        JSpinner spQty = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JButton btnAdd = new JButton("Tambah ke Keranjang");
        JLabel info = new JLabel("<html><small>Max 4 item berbeda di keranjang</small></html>");
        JButton btnRemove = new JButton("Hapus Item Terpilih");
        // tombol Kembali akan ditempatkan di paling bawah tengah (di bawah footer)

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        controlsPanel.add(lblQty, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        controlsPanel.add(spQty, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        controlsPanel.add(btnAdd, gbc);

        gbc.gridy = 2;
        controlsPanel.add(info, gbc);

        gbc.gridy = 3;
        controlsPanel.add(btnRemove, gbc);

        // Cart table (kanan bawah)
        cartModel = new DefaultTableModel(new Object[]{"No", "Nama", "Qty", "Harga", "Total"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        cartTable = new JTable(cartModel);
        cartTable.setFillsViewportHeight(true);
        JScrollPane cartScroll = new JScrollPane(cartTable);
        cartScroll.setPreferredSize(new Dimension(520, 420));

        // Right panel: controls on top, cart below
        JPanel rightPanel = new JPanel(new BorderLayout(8,8));
        rightPanel.setPreferredSize(new Dimension(620, 600));
        rightPanel.add(controlsPanel, BorderLayout.NORTH);
        rightPanel.add(cartScroll, BorderLayout.CENTER);

        // Split pane to allow responsive resizing
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menuScroll, rightPanel);
        split.setResizeWeight(0.5);
        split.setOneTouchExpandable(true);
        add(split, BorderLayout.CENTER);

        // Footer: actions (akan ditempatkan di bagian bawah, dengan tombol Kembali di bawahnya)
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnCalc = new JButton("Hitung Total");
        JButton btnPrint = new JButton("Cetak Struk");
        footer.add(btnCalc);
        footer.add(btnPrint);

        // Tombol Kembali paling bawah tengah
        JButton btnBack = new JButton("Kembali");
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backPanel.add(btnBack);

        // Gabungkan footer dan backPanel dalam satu panel vertikal di BorderLayout.SOUTH
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(footer, BorderLayout.NORTH);
        bottomPanel.add(backPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Actions
        btnAdd.addActionListener(e -> {
            int sel = menuTable.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(this, "Pilih menu terlebih dahulu.");
                return;
            }
            if (cart.size() >= 4) {
                JOptionPane.showMessageDialog(this, "Maksimal 4 item berbeda di keranjang.");
                return;
            }
            int modelIndex = menuTable.convertRowIndexToModel(sel);
            MenuItem m = MenuData.getMenuList().get(modelIndex);
            int qty = (Integer) spQty.getValue();

            boolean found = false;
            for (OrderItem oi : cart) {
                if (oi.getMenu().getName().equals(m.getName())) {
                    oi.setQty(oi.getQty() + qty);
                    found = true;
                    break;
                }
            }
            if (!found) cart.add(new OrderItem(m, qty));
            refreshCartTable();
        });

        btnRemove.addActionListener(e -> {
            int sel = cartTable.getSelectedRow();
            if (sel == -1) {
                JOptionPane.showMessageDialog(this, "Pilih item di keranjang untuk dihapus.");
                return;
            }
            int modelIndex = cartTable.convertRowIndexToModel(sel);
            if (modelIndex >= 0 && modelIndex < cart.size()) {
                cart.remove(modelIndex);
                refreshCartTable();
            }
        });

        btnCalc.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keranjang kosong.");
                return;
            }
            Calculator.Result res = Calculator.calculate(cart);
            StringBuilder sb = new StringBuilder();
            sb.append("Subtotal: Rp ").append(format(res.subtotal)).append("\n");
            sb.append("Diskon: Rp ").append(format(res.discount)).append("\n");
            sb.append("Subtotal setelah Diskon: Rp ").append(format(res.afterDiscount)).append("\n");
            sb.append("Pajak (10%): Rp ").append(format(res.tax)).append("\n");
            sb.append("Biaya Pelayanan: Rp ").append(format(res.service)).append("\n");
            sb.append("TOTAL BAYAR: Rp ").append(format(res.total)).append("\n");
            JOptionPane.showMessageDialog(this, sb.toString(), "Rincian Pembayaran", JOptionPane.INFORMATION_MESSAGE);
        });

        btnPrint.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Keranjang kosong.");
                return;
            }
            Calculator.Result res = Calculator.calculate(cart);
            ReceiptFrame rf = new ReceiptFrame(this, cart, res);
            rf.setLocationRelativeTo(this);
            rf.setVisible(true);
            this.setVisible(false);
        });

        btnBack.addActionListener(e -> {
            this.setVisible(false);
            parent.setVisible(true);
        });
    }

    private void refreshMenuTable() {
        menuModel.setRowCount(0);
        int i = 0;
        for (MenuItem m : MenuData.getMenuList()) {
            menuModel.addRow(new Object[]{i, m.getName(), m.getCategory(), format(m.getPrice())});
            i++;
        }
    }

    private void refreshCartTable() {
        cartModel.setRowCount(0);
        int i = 1;
        for (OrderItem oi : cart) {
            cartModel.addRow(new Object[]{i, oi.getMenu().getName(), oi.getQty(), format(oi.getMenu().getPrice()), format(oi.getTotal())});
            i++;
        }
    }

    private String format(int value) {
        return String.format("%,d", value).replace(',', '.');
    }
}
