-- Create users table
CREATE TABLE users (
  username VARCHAR2(50) PRIMARY KEY,
  password VARCHAR2(50) NOT NULL
);

-- Insert dummy user
INSERT INTO users (username, password) VALUES ('admin', 'admin123');

-- Create employees table
CREATE TABLE employees (
  emp_id VARCHAR2(10) PRIMARY KEY,
  name VARCHAR2(100),
  gender VARCHAR2(10),
  department VARCHAR2(50),
  email VARCHAR2(100),
  phone VARCHAR2(15),
  joining_date DATE
); 