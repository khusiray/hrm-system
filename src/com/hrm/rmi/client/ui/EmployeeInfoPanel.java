// File: com/hrm/rmi/client/ui/EmployeeInfoPanel.java
package com.hrm.rmi.client.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.hrm.rmi.client.ClientUtils;
import com.hrm.rmi.common.model.Employee;

public class EmployeeInfoPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JTextField idField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField icPassportField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField dateHiredField;
    private JTextField departmentField;
    private JTextField positionField;
    
    private JButton updateButton;
    private JButton refreshButton;
    
    private Employee currentEmployee;
    
    public EmployeeInfoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        initComponents();
        loadEmployeeData();
    }
    
    private void initComponents() {
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Employee Information");
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(new TitledBorder("Personal Details"));
        
        formPanel.add(new JLabel("Employee ID:"));
        idField = new JTextField();
        idField.setEditable(false);
        formPanel.add(idField);
        
        formPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        formPanel.add(firstNameField);
        
        formPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        formPanel.add(lastNameField);
        
        formPanel.add(new JLabel("IC/Passport:"));
        icPassportField = new JTextField();
        icPassportField.setEditable(false); // IC/Passport should not be editable
        formPanel.add(icPassportField);
        
        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);
        
        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);
        
        formPanel.add(new JLabel("Date Hired (YYYY-MM-DD):"));
        dateHiredField = new JTextField();
        formPanel.add(dateHiredField);
        
        formPanel.add(new JLabel("Department:"));
        departmentField = new JTextField();
        formPanel.add(departmentField);
        
        formPanel.add(new JLabel("Position:"));
        positionField = new JTextField();
        formPanel.add(positionField);
        
        add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployeeInfo();
            }
        });
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadEmployeeData();
            }
        });
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(updateButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadEmployeeData() {
        try {
            int employeeId = ClientUtils.getCurrentEmployeeId();
            
            if (employeeId > 0) {
                currentEmployee = ClientUtils.getEmployeeService().getEmployeeById(employeeId);
                
                if (currentEmployee != null) {
                    idField.setText(String.valueOf(currentEmployee.getEmployeeId()));
                    firstNameField.setText(currentEmployee.getFirstName());
                    lastNameField.setText(currentEmployee.getLastName());
                    icPassportField.setText(currentEmployee.getIcPassport());
                    emailField.setText(currentEmployee.getEmail() != null ? currentEmployee.getEmail() : "");
                    phoneField.setText(currentEmployee.getPhone() != null ? currentEmployee.getPhone() : "");
                    
                    if (currentEmployee.getDateHired() != null) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateHiredField.setText(dateFormat.format(currentEmployee.getDateHired()));
                    } else {
                        dateHiredField.setText("");
                    }
                    
                    departmentField.setText(currentEmployee.getDepartment() != null ? currentEmployee.getDepartment() : "");
                    positionField.setText(currentEmployee.getPosition() != null ? currentEmployee.getPosition() : "");
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateEmployeeInfo() {
        try {
            if (currentEmployee == null) {
                JOptionPane.showMessageDialog(this, "No employee data loaded.", 
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
            
            // Update employee object
            currentEmployee.setFirstName(firstName);
            currentEmployee.setLastName(lastName);
            currentEmployee.setEmail(emailField.getText().trim());
            currentEmployee.setPhone(phoneField.getText().trim());
            
            // Parse date
            String dateHiredStr = dateHiredField.getText().trim();
            if (!dateHiredStr.isEmpty()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date dateHired = dateFormat.parse(dateHiredStr);
                    currentEmployee.setDateHired(dateHired);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD.", 
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                currentEmployee.setDateHired(null);
            }
            
            currentEmployee.setDepartment(departmentField.getText().trim());
            currentEmployee.setPosition(positionField.getText().trim());
            
            // Save to server
            boolean success = ClientUtils.getEmployeeService().updateEmployee(currentEmployee);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Employee information updated successfully.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data
                loadEmployeeData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update employee information.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating employee information: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}