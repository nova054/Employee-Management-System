package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame {
    private String username;

    public Dashboard(String username) {
        this.username = username;
        setTitle("Employee Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // JMenuBar setup
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.setMnemonic('L');
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setMnemonic('E');
        fileMenu.add(logoutItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.setMnemonic('A');
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);

        JLabel welcomeLabel = new JLabel("Welcome, " + username + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(welcomeLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton registerBtn = new JButton("Register Employee");
        JButton viewBtn = new JButton("View Employees");

        buttonPanel.add(registerBtn);
        buttonPanel.add(viewBtn);
        add(buttonPanel, BorderLayout.CENTER);

        registerBtn.addActionListener(e -> {
            dispose();
            new RegisterEmployee();
        });
        viewBtn.addActionListener(e -> {
            dispose();
            new ViewEmployees();
        });

        // Menu item actions
        logoutItem.addActionListener(e -> {
            dispose();
            new LoginPage();
        });
        exitItem.addActionListener(e -> System.exit(0));
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Employee Management System\nDeveloped with Java Swing\n2024",
                "About", JOptionPane.INFORMATION_MESSAGE));

        setVisible(true);

        // Add WindowListener to show message on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(Dashboard.this, "Thank you for using the Employee Management System!", "Goodbye", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
} 