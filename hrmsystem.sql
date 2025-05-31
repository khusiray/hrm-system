-- Create database
CREATE DATABASE IF NOT EXISTS hrm_system;
USE hrm_system;

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    user_type ENUM('EMPLOYEE', 'HR') NOT NULL,
    employee_id INT NULL
);

-- Employee table
CREATE TABLE IF NOT EXISTS employees (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    ic_passport VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    date_hired DATE,
    department VARCHAR(50),
    position VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Family details table
CREATE TABLE IF NOT EXISTS family_details (
    family_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    relation_type VARCHAR(20) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    contact_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE
);

-- Leave types table
CREATE TABLE IF NOT EXISTS leave_types (
    leave_type_id INT AUTO_INCREMENT PRIMARY KEY,
    leave_type_name VARCHAR(50) UNIQUE NOT NULL,
    default_days INT NOT NULL
);

-- Leave applications table
CREATE TABLE IF NOT EXISTS leave_applications (
    leave_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    leave_type_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_days INT NOT NULL,
    reason TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE,
    FOREIGN KEY (leave_type_id) REFERENCES leave_types(leave_type_id)
);

-- Leave balance table
CREATE TABLE IF NOT EXISTS leave_balances (
    balance_id INT AUTO_INCREMENT PRIMARY KEY,
    employee_id INT NOT NULL,
    leave_type_id INT NOT NULL,
    year INT NOT NULL,
    total_days INT NOT NULL,
    used_days INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY (employee_id, leave_type_id, year),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id) ON DELETE CASCADE,
    FOREIGN KEY (leave_type_id) REFERENCES leave_types(leave_type_id)
);

-- Insert default leave types
INSERT INTO leave_types (leave_type_name, default_days) VALUES
('Annual Leave', 14),
('Sick Leave', 14),
('Maternity Leave', 60),
('Paternity Leave', 7),
('Compassionate Leave', 3);

-- Insert default HR user
INSERT INTO employees (first_name, last_name, ic_passport, email, department, position)
VALUES ('Admin', 'HR', 'ADMIN001', 'admin@bhel.com', 'Human Resources', 'HR Manager');

INSERT INTO users (username, password, user_type, employee_id)
VALUES ('admin', 'admin123', 'HR', 1);
