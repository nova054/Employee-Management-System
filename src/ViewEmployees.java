package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewEmployees extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> searchTypeBox;

    public ViewEmployees() {
        setTitle("View Employees");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchTypeBox = new JComboBox<>(new String[]{"ID", "Name", "Department"});
        searchField = new JTextField(15);
        // Add KeyListener for Enter key
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchEmployees();
                }
            }
        });
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton backBtn = new JButton("Back");
        topPanel.add(new JLabel("Search by:"));
        topPanel.add(searchTypeBox);
        topPanel.add(searchField);
        topPanel.add(searchBtn);
        topPanel.add(refreshBtn);
        topPanel.add(editBtn);
        topPanel.add(deleteBtn);
        topPanel.add(backBtn);
        add(topPanel, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[]{"ID", "Name", "Gender", "Department", "Email", "Phone", "Joining Date"}, 0);
        table = new JTable(model);
        // Add MouseListener for double-click to edit
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String empId = (String) model.getValueAt(table.getSelectedRow(), 0);
                    dispose();
                    new EditEmployee(empId);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        loadEmployees("");

        searchBtn.addActionListener(e -> searchEmployees());
        refreshBtn.addActionListener(e -> loadEmployees(""));
        backBtn.addActionListener(e -> {
            dispose();
            new Dashboard("Admin");
        });
        editBtn.addActionListener(e -> editSelectedEmployee());
        deleteBtn.addActionListener(e -> deleteSelectedEmployee());

        setVisible(true);
    }

    private void loadEmployees(String whereClause) {
        model.setRowCount(0);
        String sql = "SELECT * FROM employees" + (whereClause.isEmpty() ? "" : " WHERE " + whereClause);
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("emp_id"),
                        rs.getString("name"),
                        rs.getString("gender"),
                        rs.getString("department"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getDate("joining_date")
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchEmployees() {
        String type = (String) searchTypeBox.getSelectedItem();
        String value = searchField.getText().trim();
        if (value.isEmpty()) {
            loadEmployees("");
            return;
        }
        String where = "";
        switch (type) {
            case "ID":
                where = "emp_id LIKE '" + value + "%'";
                break;
            case "Name":
                where = "LOWER(name) LIKE '" + value.toLowerCase() + "%'";
                break;
            case "Department":
                where = "LOWER(department) LIKE '" + value.toLowerCase() + "%'";
                break;
        }
        loadEmployees(where);
    }

    private void editSelectedEmployee() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String empId = (String) model.getValueAt(row, 0);
        dispose();
        new EditEmployee(empId);
    }

    private void deleteSelectedEmployee() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an employee to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String empId = (String) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete employee ID: " + empId + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM employees WHERE emp_id = ?");
                ps.setString(1, empId);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Employee deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadEmployees("");
                } else {
                    JOptionPane.showMessageDialog(this, "Delete failed.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
} 