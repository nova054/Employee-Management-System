package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditEmployee extends JFrame {
    private JTextField empIdField, nameField, emailField, phoneField, dateField;
    private JComboBox<String> deptBox;
    private JRadioButton maleBtn, femaleBtn, otherBtn;
    private ButtonGroup genderGroup;
    private JLabel statusLabel;
    private String empId;

    public EditEmployee(String empId) {
        this.empId = empId;
        setTitle("Edit Employee");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        panel.add(new JLabel("Employee ID:"), gbc(0, y));
        empIdField = new JTextField(15);
        empIdField.setEditable(false);
        panel.add(empIdField, gbc(1, y++));

        panel.add(new JLabel("Full Name:"), gbc(0, y));
        nameField = new JTextField(15);
        panel.add(nameField, gbc(1, y++));

        panel.add(new JLabel("Gender:"), gbc(0, y));
        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        maleBtn = new JRadioButton("Male");
        femaleBtn = new JRadioButton("Female");
        otherBtn = new JRadioButton("Other");
        genderGroup = new ButtonGroup();
        genderGroup.add(maleBtn); genderGroup.add(femaleBtn); genderGroup.add(otherBtn);
        genderPanel.add(maleBtn); genderPanel.add(femaleBtn); genderPanel.add(otherBtn);
        panel.add(genderPanel, gbc(1, y++));

        panel.add(new JLabel("Department:"), gbc(0, y));
        deptBox = new JComboBox<>(new String[]{"HR", "IT", "Finance", "Sales", "Admin"});
        panel.add(deptBox, gbc(1, y++));

        panel.add(new JLabel("Email:"), gbc(0, y));
        emailField = new JTextField(15);
        panel.add(emailField, gbc(1, y++));

        panel.add(new JLabel("Phone Number:"), gbc(0, y));
        phoneField = new JTextField(15);
        panel.add(phoneField, gbc(1, y++));

        panel.add(new JLabel("Date of Joining (yyyy-mm-dd):"), gbc(0, y));
        dateField = new JTextField(15);
        panel.add(dateField, gbc(1, y++));

        JButton saveBtn = new JButton("Save Changes");
        gbc.gridx = 1; gbc.gridy = y++;
        panel.add(saveBtn, gbc);

        statusLabel = new JLabel("");
        statusLabel.setForeground(Color.RED);
        gbc.gridx = 1; gbc.gridy = y;
        panel.add(statusLabel, gbc);

        add(panel, BorderLayout.CENTER);

        saveBtn.addActionListener(e -> saveChanges());

        loadEmployeeData();
        setVisible(true);
    }

    private GridBagConstraints gbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = x;
        gbc.gridy = y;
        return gbc;
    }

    private void loadEmployeeData() {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM employees WHERE emp_id = ?");
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                empIdField.setText(rs.getString("emp_id"));
                nameField.setText(rs.getString("name"));
                String gender = rs.getString("gender");
                if ("Male".equalsIgnoreCase(gender)) maleBtn.setSelected(true);
                else if ("Female".equalsIgnoreCase(gender)) femaleBtn.setSelected(true);
                else otherBtn.setSelected(true);
                deptBox.setSelectedItem(rs.getString("department"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));
                dateField.setText(rs.getDate("joining_date").toString());
            }
        } catch (Exception ex) {
            statusLabel.setText("DB Error: " + ex.getMessage());
        }
    }

    private void saveChanges() {
        String name = nameField.getText().trim();
        String gender = maleBtn.isSelected() ? "Male" : femaleBtn.isSelected() ? "Female" : otherBtn.isSelected() ? "Other" : "";
        String dept = (String) deptBox.getSelectedItem();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String dateStr = dateField.getText().trim();

        // Validation
        if (name.isEmpty() || gender.isEmpty() || dept.isEmpty() || email.isEmpty() || phone.isEmpty() || dateStr.isEmpty()) {
            statusLabel.setText("All fields are required.");
            return;
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            statusLabel.setText("Invalid email format.");
            return;
        }
        if (!phone.matches("\\d{10}")) {
            statusLabel.setText("Phone must be 10 digits.");
            return;
        }
        java.sql.Date sqlDate;
        try {
            Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
            sqlDate = new java.sql.Date(utilDate.getTime());
        } catch (ParseException e) {
            statusLabel.setText("Invalid date format.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE employees SET name=?, gender=?, department=?, email=?, phone=?, joining_date=? WHERE emp_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, gender);
            ps.setString(3, dept);
            ps.setString(4, email);
            ps.setString(5, phone);
            ps.setDate(6, sqlDate);
            ps.setString(7, empId);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new ViewEmployees();
            } else {
                statusLabel.setText("Update failed.");
            }
        } catch (Exception ex) {
            statusLabel.setText("DB Error: " + ex.getMessage());
        }
    }
} 