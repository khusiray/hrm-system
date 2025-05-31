package com.hrm.rmi.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.hrm.rmi.client.ClientUtils;
import com.hrm.rmi.common.model.Employee;
import com.hrm.rmi.common.model.LeaveApplication;
import com.hrm.rmi.common.model.User;

public class EmployeeManagementPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField icPassportField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField dateHiredField;
    private JTextField departmentField;
    private JTextField positionField;
    
    private JTable employeeTable;
    private DefaultTableModel employeeTableModel;
    
    private JTable leaveTable;
    private DefaultTableModel leaveTableModel;
    
    private JButton registerButton;
    private JButton updateButton;
    private JButton clearButton;
    private JButton refreshButton;
    private JButton approveButton;
    private JButton rejectButton;
    
    private List<Employee> employees;
    private List<LeaveApplication> leaveApplications;
    private Employee selectedEmployee;
    private LeaveApplication selectedLeaveApplication;
    
    private boolean isHRAdmin() {
        return ClientUtils.isHR() && 
               ClientUtils.getCurrentUser() != null && 
               "admin".equals(ClientUtils.getCurrentUser().getUsername());
    }
    
    private static final String[] EMPLOYEE_COLUMNS = {
        "ID", "First Name", "Last Name", "IC/Passport", "Department", "Position"
    };
    
    private static final String[] LEAVE_COLUMNS = {
        "ID", "Employee ID", "Employee Name", "Leave Type", "Start Date", "End Date", "Total Days", "Status"
    };
    
    public EmployeeManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Employee Management");
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Main panel (split into top and bottom sections using JSplitPane)
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setDividerLocation(400); // Set initial divider location
        mainSplitPane.setResizeWeight(0.5); // Split space evenly
        
        // Top section (employee registration and table)
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setPreferredSize(new Dimension(800, 400));
        
        // Registration form
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(new TitledBorder("Employee Registration/Update"));
        
        // Form fields
        JPanel fieldsPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        
        fieldsPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        fieldsPanel.add(firstNameField);
        
        fieldsPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        fieldsPanel.add(lastNameField);
        
        fieldsPanel.add(new JLabel("IC/Passport:"));
        icPassportField = new JTextField();
        fieldsPanel.add(icPassportField);
        
        fieldsPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        fieldsPanel.add(emailField);
        
        fieldsPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        fieldsPanel.add(phoneField);
        
        fieldsPanel.add(new JLabel("Date Hired (YYYY-MM-DD):"));
        dateHiredField = new JTextField();
        fieldsPanel.add(dateHiredField);
        
        fieldsPanel.add(new JLabel("Department:"));
        departmentField = new JTextField();
        fieldsPanel.add(departmentField);
        
        fieldsPanel.add(new JLabel("Position:"));
        positionField = new JTextField();
        fieldsPanel.add(positionField);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isHRAdmin()) {
                    registerEmployee();
                } else {
                    JOptionPane.showMessageDialog(EmployeeManagementPanel.this, 
                        "Only admin users can register new employees.", 
                        "Access Denied", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isHRAdmin() || selectedEmployee.getEmployeeId() == ClientUtils.getCurrentEmployeeId()) {
                    updateEmployee();
                } else {
                    JOptionPane.showMessageDialog(EmployeeManagementPanel.this, 
                        "Only admin users can update other employees.", 
                        "Access Denied", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        
        buttonPanel.add(registerButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(clearButton);
        
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Employee table
        JPanel employeeTablePanel = new JPanel(new BorderLayout());
        employeeTablePanel.setBorder(new TitledBorder("Employees"));
        
        employeeTableModel = new DefaultTableModel(EMPLOYEE_COLUMNS, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        employeeTable = new JTable(employeeTableModel);
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && employeeTable.getSelectedRow() != -1) {
                int selectedRow = employeeTable.getSelectedRow();
                selectEmployee(selectedRow);
            }
        });
        
        JScrollPane employeeScrollPane = new JScrollPane(employeeTable);
        employeeTablePanel.add(employeeScrollPane, BorderLayout.CENTER);
        
        // Refresh button panel
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        refreshPanel.add(refreshButton);
        employeeTablePanel.add(refreshPanel, BorderLayout.SOUTH);
        
        // Layout top section
        topPanel.add(formPanel, BorderLayout.WEST);
        topPanel.add(employeeTablePanel, BorderLayout.CENTER);
        
        // Bottom section (leave applications)
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(new TitledBorder("Leave Applications"));
        bottomPanel.setPreferredSize(new Dimension(800, 200));
        
        leaveTableModel = new DefaultTableModel(LEAVE_COLUMNS, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        leaveTable = new JTable(leaveTableModel);
        leaveTable.getTableHeader().setReorderingAllowed(false);
        leaveTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && leaveTable.getSelectedRow() != -1) {
                int selectedRow = leaveTable.getSelectedRow();
                selectLeaveApplication(selectedRow);
            }
        });
        
        JScrollPane leaveScrollPane = new JScrollPane(leaveTable);
        bottomPanel.add(leaveScrollPane, BorderLayout.CENTER);
        
        // Leave approval buttons
        JPanel leaveButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        approveButton = new JButton("Approve");
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLeaveStatus("APPROVED");
            }
        });
        
        rejectButton = new JButton("Reject");
        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLeaveStatus("REJECTED");
            }
        });
        
        leaveButtonPanel.add(approveButton);
        leaveButtonPanel.add(rejectButton);
        
        bottomPanel.add(leaveButtonPanel, BorderLayout.SOUTH);
        
        // Add to split pane
        mainSplitPane.setTopComponent(topPanel);
        mainSplitPane.setBottomComponent(bottomPanel);
        
        add(mainSplitPane, BorderLayout.CENTER);
    }
    
    private void loadData() {
        try {
            // Load employees
            employees = ClientUtils.getEmployeeService().getAllEmployees();
            
            // Clear tables
            employeeTableModel.setRowCount(0);
            
            // Add data to employee table
            for (Employee employee : employees) {
                Object[] rowData = {
                    employee.getEmployeeId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getIcPassport(),
                    employee.getDepartment() != null ? employee.getDepartment() : "",
                    employee.getPosition() != null ? employee.getPosition() : ""
                };
                employeeTableModel.addRow(rowData);
            }
            
            // Load leave applications
            leaveApplications = ClientUtils.getEmployeeService().getAllLeaveApplications();
            
            // Clear leave table
            leaveTableModel.setRowCount(0);
            
            // Date formatter
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            // Add data to leave table
            for (LeaveApplication application : leaveApplications) {
                // Get employee name
                String employeeName = "";
                for (Employee emp : employees) {
                    if (emp.getEmployeeId() == application.getEmployeeId()) {
                        employeeName = emp.getFirstName() + " " + emp.getLastName();
                        break;
                    }
                }
                
                Object[] rowData = {
                    application.getLeaveId(),
                    application.getEmployeeId(),
                    employeeName,
                    application.getLeaveTypeName(),
                    dateFormat.format(application.getStartDate()),
                    dateFormat.format(application.getEndDate()),
                    application.getTotalDays(),
                    application.getStatus()
                };
                leaveTableModel.addRow(rowData);
            }
            
            // Clear form
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void selectEmployee(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < employees.size()) {
            selectedEmployee = employees.get(selectedRow);
            
            // Populate form fields
            firstNameField.setText(selectedEmployee.getFirstName());
            lastNameField.setText(selectedEmployee.getLastName());
            icPassportField.setText(selectedEmployee.getIcPassport());
            emailField.setText(selectedEmployee.getEmail() != null ? selectedEmployee.getEmail() : "");
            phoneField.setText(selectedEmployee.getPhone() != null ? selectedEmployee.getPhone() : "");
            
            if (selectedEmployee.getDateHired() != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateHiredField.setText(dateFormat.format(selectedEmployee.getDateHired()));
            } else {
                dateHiredField.setText("");
            }
            
            departmentField.setText(selectedEmployee.getDepartment() != null ? selectedEmployee.getDepartment() : "");
            positionField.setText(selectedEmployee.getPosition() != null ? selectedEmployee.getPosition() : "");
            
            // Disable IC/Passport field when updating
            icPassportField.setEditable(false);
        }
    }
    
    private void selectLeaveApplication(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < leaveApplications.size()) {
            selectedLeaveApplication = leaveApplications.get(selectedRow);
            
            // Enable/disable buttons based on status
            boolean isPending = "PENDING".equals(selectedLeaveApplication.getStatus());
            approveButton.setEnabled(isPending);
            rejectButton.setEnabled(isPending);
        }
    }
    
    private void registerEmployee() {
        try {
            // Validate fields
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String icPassport = icPassportField.getText().trim();
            
            if (firstName.isEmpty() || lastName.isEmpty() || icPassport.isEmpty()) {
                JOptionPane.showMessageDialog(this, "First name, last name, and IC/Passport are required.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if IC/Passport already exists
            if (ClientUtils.getEmployeeService().getEmployeeByIcPassport(icPassport) != null) {
                JOptionPane.showMessageDialog(this, "An employee with this IC/Passport already exists.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create new employee
            Employee newEmployee = new Employee();
            newEmployee.setFirstName(firstName);
            newEmployee.setLastName(lastName);
            newEmployee.setIcPassport(icPassport);
            newEmployee.setEmail(emailField.getText().trim());
            newEmployee.setPhone(phoneField.getText().trim());
            
            // Parse date
            String dateHiredStr = dateHiredField.getText().trim();
            if (!dateHiredStr.isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateHired = dateFormat.parse(dateHiredStr);
                    newEmployee.setDateHired(dateHired);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", 
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            
            newEmployee.setDepartment(departmentField.getText().trim());
            newEmployee.setPosition(positionField.getText().trim());
            
            // Register employee
            int employeeId = ClientUtils.getEmployeeService().registerEmployee(newEmployee);
            
            if (employeeId > 0) {
                // Create user account for employee - IMPORTANT: store password as plain text
            	User user = new User();
            	user.setUsername(icPassport); // Use IC/Passport as default username
            	user.setPassword(icPassport); // Use IC/Passport as default password
            	user.setUserType("EMPLOYEE"); // Set type to EMPLOYEE
            	user.setEmployeeId(employeeId);

            	System.out.println("Creating employee user: " + user.getUsername() + ", Password: " + user.getPassword());
            	boolean userCreated = ClientUtils.getAuthService().createUser(user);

                
                if (userCreated) {
                    JOptionPane.showMessageDialog(this, 
                            "Employee registered successfully. \n" +
                            "Default username and password are set to the IC/Passport number.", 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Initialize leave balances for current year
                    int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
                    ClientUtils.getEmployeeService().initializeLeaveBalances(employeeId, currentYear);
                    
                    // Refresh data
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Employee registered but failed to create user account.", 
                            "Partial Success", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to register employee.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error registering employee: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
       
       private void updateEmployee() {
           try {
               if (selectedEmployee == null) {
                   JOptionPane.showMessageDialog(this, "Please select an employee to update.", 
                           "Error", JOptionPane.ERROR_MESSAGE);
                   return;
               }
               
               // Validate fields
               String firstName = firstNameField.getText().trim();
               String lastName = lastNameField.getText().trim();
               
               if (firstName.isEmpty() || lastName.isEmpty()) {
                   JOptionPane.showMessageDialog(this, "First name and last name are required.", 
                           "Validation Error", JOptionPane.ERROR_MESSAGE);
                   return;
               }
               
               // Update employee
               selectedEmployee.setFirstName(firstName);
               selectedEmployee.setLastName(lastName);
               selectedEmployee.setEmail(emailField.getText().trim());
               selectedEmployee.setPhone(phoneField.getText().trim());
               
               // Parse date
               String dateHiredStr = dateHiredField.getText().trim();
               if (!dateHiredStr.isEmpty()) {
                   try {
                       SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                       Date dateHired = dateFormat.parse(dateHiredStr);
                       selectedEmployee.setDateHired(dateHired);
                   } catch (Exception e) {
                       JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", 
                               "Validation Error", JOptionPane.ERROR_MESSAGE);
                       return;
                   }
               } else {
                   selectedEmployee.setDateHired(null);
               }
               
               selectedEmployee.setDepartment(departmentField.getText().trim());
               selectedEmployee.setPosition(positionField.getText().trim());
               
               // Save to server
               boolean success = ClientUtils.getEmployeeService().updateEmployee(selectedEmployee);
               
               if (success) {
                   JOptionPane.showMessageDialog(this, "Employee information updated successfully.", 
                           "Success", JOptionPane.INFORMATION_MESSAGE);
                   
                   // Refresh data
                   loadData();
               } else {
                   JOptionPane.showMessageDialog(this, "Failed to update employee information.", 
                           "Error", JOptionPane.ERROR_MESSAGE);
               }
           } catch (Exception e) {
               JOptionPane.showMessageDialog(this, "Error updating employee: " + e.getMessage(), 
                       "Error", JOptionPane.ERROR_MESSAGE);
               e.printStackTrace();
           }
       }
       
       private void updateLeaveStatus(String status) {
           try {
               if (selectedLeaveApplication == null) {
                   JOptionPane.showMessageDialog(this, "Please select a leave application.", 
                           "Error", JOptionPane.ERROR_MESSAGE);
                   return;
               }
               
               // Confirm status change
               int confirmation = JOptionPane.showConfirmDialog(this, 
                       "Are you sure you want to " + status.toLowerCase() + " this leave application?", 
                       "Confirm", JOptionPane.YES_NO_OPTION);
               
               if (confirmation != JOptionPane.YES_OPTION) {
                   return;
               }
               
               // Update status on server
               boolean success = ClientUtils.getEmployeeService().updateLeaveStatus(
                       selectedLeaveApplication.getLeaveId(), status);
               
               if (success) {
                   JOptionPane.showMessageDialog(this, "Leave application status updated successfully.", 
                           "Success", JOptionPane.INFORMATION_MESSAGE);
                   
                   // Refresh data
                   loadData();
                   
                   // Clear selection
                   selectedLeaveApplication = null;
                   leaveTable.clearSelection();
               } else {
                   JOptionPane.showMessageDialog(this, "Failed to update leave application status.", 
                           "Error", JOptionPane.ERROR_MESSAGE);
               }
           } catch (Exception e) {
               JOptionPane.showMessageDialog(this, "Error updating leave status: " + e.getMessage(), 
                       "Error", JOptionPane.ERROR_MESSAGE);
               e.printStackTrace();
           }
       }
       
       private void clearForm() {
           selectedEmployee = null;
           firstNameField.setText("");
           lastNameField.setText("");
           icPassportField.setText("");
           icPassportField.setEditable(true); // Enable for new registration
           emailField.setText("");
           phoneField.setText("");
           dateHiredField.setText("");
           departmentField.setText("");
           positionField.setText("");
           employeeTable.clearSelection();
       }
   }