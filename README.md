# Employee Management System

## Introduction

The Employee Management System is a desktop application built with Java Swing, designed to manage employee records efficiently. It features a user-friendly GUI, robust validation, and persistent storage using an Oracle database. The application demonstrates practical use of Java Swing components, event handling, and modular design.

## Objectives

- Provide a secure login system for administrators.
- Allow registration, viewing, editing, and deletion of employee records.
- Demonstrate advanced Java Swing usage: frames, panels, tables, dialogs, and event listeners.
- Ensure modular, maintainable, and extensible code.

---

## Technology Stack

- **GUI:** Java Swing (`JFrame`, `JPanel`, `JTable`, dialogs, menus)
- **Database:** Oracle (via JDBC)
- **Event Handling:** ActionListener, KeyListener, MouseListener, ItemListener, WindowListener
- **OOP:** Modular structure with clear separation of concerns

---

## Project Structure

```
src/
  ├── CreateTables.sql
  ├── DBConnection.java
  ├── DBConnectionTest.java
  ├── Dashboard.java
  ├── EditEmployee.java
  ├── EmployeeManagementSystem.java
  ├── LoginPage.java
  ├── Main.java
  ├── RegisterEmployee.java
  ├── ViewEmployees.java
```

---

## File Descriptions & Swing Usage

### 1. `Main.java` — Application Entry Point

```java
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginPage();
        });
    }
}
```
- Launches the application on the Swing event dispatch thread for thread safety.
- Starts with the login page.

---

### 2. `LoginPage.java` — Login Window

- **Purpose:** Authenticates users before granting access to the dashboard.
- **Swing Features:** Uses `JFrame`, `JPanel`, `JLabel`, `JTextField`, `JPasswordField`, `JButton`, and layout managers.
- **Event Handling:** 
  - `ActionListener` for login button and password field (login on click or Enter).
  - `KeyListener` for login button (login on Enter key).
- **Example:**
  ```java
  JButton loginButton = new JButton("Login");
  loginButton.addActionListener(e -> login());
  loginButton.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              login();
          }
      }
  });
  ```

---

### 3. `Dashboard.java` — Main Menu

- **Purpose:** Central navigation for the application.
- **Swing Features:** Uses `JFrame`, `JPanel`, `JLabel`, `JButton`, `GridLayout`, `BorderLayout`, and now a `JMenuBar` with mnemonics.
- **Event Handling:** 
  - `ActionListener` for navigation buttons (Register, View).
  - `JMenuBar` with File and Help menus, each with mnemonics (Alt+F, Alt+H) and menu items (Logout, Exit, About) with mnemonics (Alt+L, Alt+E, Alt+A).
  - `WindowListener` to show a message when the window is closing.
- **Example:**
  ```java
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

  // Menu item actions
  logoutItem.addActionListener(e -> {
      dispose();
      new LoginPage();
  });
  exitItem.addActionListener(e -> System.exit(0));
  aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
          "Employee Management System\nDeveloped with Java Swing\n2024",
          "About", JOptionPane.INFORMATION_MESSAGE));
  ```

---

### 4. `RegisterEmployee.java` — Employee Registration

- **Purpose:** Allows adding new employees.
- **Swing Features:** Uses `JFrame`, `JPanel`, `JLabel`, `JTextField`, `JComboBox`, `JRadioButton`, `ButtonGroup`, and `JButton`.
- **Event Handling:** 
  - `ActionListener` for the register button.
  - `ItemListener` for department combo box (shows a message when changed).
- **Example:**
  ```java
  deptBox.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
          if (e.getStateChange() == ItemEvent.SELECTED) {
              JOptionPane.showMessageDialog(RegisterEmployee.this, "Department changed to: " + e.getItem(), "Department Changed", JOptionPane.INFORMATION_MESSAGE);
          }
      }
  });
  ```

---

### 5. `ViewEmployees.java` — Employee Table & Search

- **Purpose:** Displays all employees, allows searching, editing, and deleting.
- **Swing Features:** Uses `JFrame`, `JPanel`, `JTable`, `DefaultTableModel`, `JScrollPane`, `JButton`, and `JComboBox`.
- **Event Handling:** 
  - `ActionListener` for search, refresh, edit, delete, and back buttons.
  - `KeyListener` for search field (search on Enter).
  - `MouseListener` for table (double-click to edit).
- **Example:**
  ```java
  searchField.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              searchEmployees();
          }
      }
  });
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
  ```

---

### 6. `EditEmployee.java` — Edit Employee Details

- **Purpose:** Allows editing existing employee records.
- **Swing Features:** Uses `JFrame`, `JPanel`, `JLabel`, `JTextField`, `JComboBox`, `JRadioButton`, `ButtonGroup`, and `JButton`.
- **Event Handling:** 
  - `ActionListener` for the save button.
- **Example:**
  ```java
  saveBtn.addActionListener(e -> saveChanges());
  ```

---

### 7. `DBConnection.java` — Database Utility

- **Purpose:** Provides a static method to get a JDBC connection to the Oracle database.
- **Example:**
  ```java
  public static Connection getConnection() throws SQLException {
      try {
          Class.forName("oracle.jdbc.driver.OracleDriver");
      } catch (ClassNotFoundException e) {
          throw new SQLException("Oracle JDBC Driver not found.", e);
      }
      return DriverManager.getConnection(URL, USER, PASS);
  }
  ```

---

### 8. `DBConnectionTest.java` — Database Connection Test

- **Purpose:** Simple test class to verify database connectivity.
- **Example:**
  ```java
  public class DBConnectionTest {
      public static void main(String[] args) {
          try {
              java.sql.Connection conn = DBConnection.getConnection();
              System.out.println("Database connection successful!");
              conn.close();
          } catch (Exception e) {
              System.err.println("Database connection failed: " + e.getMessage());
              e.printStackTrace();
          }
      }
  }
  ```

---

### 9. `CreateTables.sql` — Database Schema

- **Purpose:** SQL script to create the required tables (`users`, `employees`) and insert a dummy user.
- **Example:**
  ```sql
  CREATE TABLE users (
    username VARCHAR2(50) PRIMARY KEY,
    password VARCHAR2(50) NOT NULL
  );
  INSERT INTO users (username, password) VALUES ('admin', 'admin123');
  CREATE TABLE employees (
    emp_id VARCHAR2(10) PRIMARY KEY,
    name VARCHAR2(100),
    gender VARCHAR2(10),
    department VARCHAR2(50),
    email VARCHAR2(100),
    phone VARCHAR2(15),
    joining_date DATE
  );
  ```

---

## Swing & Event Handling Highlights

- **Frames & Panels:** Modular use of `JFrame` and `JPanel` for each major screen.
- **Menus & Navigation:** Dashboard now features a `JMenuBar` with File and Help menus, each with keyboard mnemonics for accessibility (Alt+F, Alt+H, etc.).
- **Tables:** Employee data is displayed and managed using `JTable` and `DefaultTableModel`.
- **Dialogs:** Uses `JOptionPane` for user feedback, confirmations, and error messages.
- **Event Listeners:** Demonstrates `ActionListener`, `KeyListener`, `MouseListener`, `ItemListener`, `WindowListener`, and now `JMenuBar` mnemonics for a rich, interactive UI.

---

## Inspiration

This project structure and documentation style is inspired by the [chulo-project/java Password Manager](https://github.com/chulo-project/java), which demonstrates advanced Java Swing usage, modularity, and best practices for desktop application development.

---

## Getting Started

1. **Set up Oracle Database** using `CreateTables.sql`.
2. **Configure DB credentials** in `DBConnection.java`.
3. **Compile all Java files** in the `src` directory.
4. **Run `Main.java`** to start the application.

---

## License

This project is for educational purposes.
