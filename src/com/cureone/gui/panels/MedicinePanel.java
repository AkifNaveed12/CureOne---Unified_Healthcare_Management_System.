package com.cureone.gui.panels;

import com.cureone.gui.dashboards.AdminDashboard;
import com.cureone.pharmacyandinventory.model.Medicine;
import com.cureone.pharmacyandinventory.service.MedicineService;
import com.cureone.gui.MainFrame;
import com.cureone.gui.util.NavigationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * MedicinePanel
 * -------------
 * Used by:
 * - PharmacistDashboard
 * - CustomerDashboard (read-only)
 */
public class MedicinePanel extends JPanel {

    private final MedicineService service;
    private final String role;
    private final int userId;
    private final MainFrame frame;

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    // ✅ Simple constructor (Customer / read-only usage)
    public MedicinePanel(MedicineService service) {
        this(service, 0, "CUSTOMER", null);
    }


    public MedicinePanel(
            MedicineService service,
            int userId,
            String role,
            MainFrame frame
    ) {
        this.service = service;
        this.userId = userId;
        this.role = role;
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(buildTop(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        loadAll();
    }


    // ================= TOP BAR =================
    private JPanel buildTop() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);

        JLabel title = new JLabel("Medicines");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(Color.WHITE);

        searchField = new JTextField(18);
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        searchBtn.addActionListener(e -> search());
        refreshBtn.addActionListener(e -> loadAll());
        backBtn.addActionListener(e -> handleBack());

//        backBtn.addActionListener(e ->
//                NavigationUtil.goBack(frame, role, userId)
//        );



        right.add(new JLabel("Name:"));
        right.add(searchField);
        right.add(searchBtn);
        right.add(refreshBtn);
        right.add(backBtn);

        top.add(title, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        return top;
    }

    // ================= TABLE =================
    private JScrollPane buildTable() {

        model = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Name",
                        "Category",
                        "Price",
                        "Quantity",
                        "Expiry Date",
                        "Manufacturer"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // always read-only
            }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return new JScrollPane(table);
    }

    // ================= LOAD ALL =================
    private void loadAll() {
        model.setRowCount(0);

        List<Medicine> medicines;

        try {
            medicines = service.getAllMedicines();
        } catch (Exception ex) {
            showDbError();
            return;
        }

        if (medicines == null || medicines.isEmpty()) {
            showEmpty("No medicines found");
            return;
        }

        for (Medicine m : medicines) {
            addRow(m);
        }
    }

    // ================= SEARCH =================
    private void search() {
        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadAll();
            return;
        }

        model.setRowCount(0);

        List<Medicine> medicines;

        try {
            medicines = service.searchByName(keyword);
        } catch (Exception ex) {
            showDbError();
            return;
        }

        if (medicines == null || medicines.isEmpty()) {
            showEmpty("No results for \"" + keyword + "\"");
            return;
        }

        for (Medicine m : medicines) {
            addRow(m);
        }
    }

    // ================= ROW HELPER =================
    private void addRow(Medicine m) {
        model.addRow(new Object[]{
                m.getId(),
                safe(m.getName()),
                m.getCategory() != null ? m.getCategory().getName() : "-",
                m.getPrice(),
                m.getQuantity(),
                m.getExpiryDate() != null ? m.getExpiryDate() : "-",
                safe(m.getManufacturer())
        });
    }

    // ================= HELPERS =================
    private void showEmpty(String msg) {
        model.addRow(new Object[]{
                "-", msg, "-", "-", "-", "-", "-"
        });
    }

    private void showDbError() {
        JOptionPane.showMessageDialog(
                this,
                "Medicine data could not be loaded.\nPlease check database.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private String safe(String v) {
        return v == null || v.isBlank() ? "-" : v;
    }

    // ================= BACK HANDLER =================
    private void handleBack() {
        Container c = this.getParent();

        while (c != null) {
            if (c instanceof com.cureone.gui.dashboards.AdminDashboard a) {
                a.showHome();
                return;
            }
            if (c instanceof com.cureone.gui.dashboards.PharmacistDashboard p) {
                p.showHome();
                return;
            }
            if (c instanceof com.cureone.gui.dashboards.CustomerDashboard cu) {
                cu.showHome();
                return;
            }
            c = c.getParent();
        }
    }

}
