// File: com/hrm/rmi/client/ui/EmployeeFormDialog.java
package com.hrm.rmi.client.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.hrm.rmi.client.ClientUtils;
import com.hrm.rmi.common.model.Employee;

public class EmployeeFormDialog extends JDialog {
    private JTextField firstNameField, lastNameField, icPassportField, emailField, phoneField, departmentField, positionField;
    private JButton saveButton, cancelButton;

    private Employee employee;
    private Runnable onSave;

    public EmployeeFormDialog(Window parent, Employee employee, Runnable onSave) {
        super(parent, "Employee Form", ModalityType.APPLICATION_MODAL);
        this.employee = employee != null ? employee : new Employee();
        this.onSave = onSave;

        setSize(400, 400);
        setLocationRelativeTo(parent);
        initUI();
        loadData();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        formPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        formPanel.add(firstNameField);

        formPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        formPanel.add(lastNameField);

        formPanel.add(new JLabel("IC/Passport:"));
        icPassportField = new JTextField();
        formPanel.add(icPassportField);

        formPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        formPanel.add(emailField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Department:"));
        departmentField = new JTextField();
        formPanel.add(departmentField);

        formPanel.add(new JLabel("Position:"));
        positionField = new JTextField();
        formPanel.add(positionField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveEmployee());
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        if (employee != null) {
            firstNameField.setText(employee.getFirstName());
            lastNameField.setText(employee.getLastName());
            icPassportField.setText(employee.getIcPassport());
            emailField.setText(employee.getEmail());
            phoneField.setText(employee.getPhone());
            departmentField.setText(employee.getDepartment());
            positionField.setText(employee.getPosition());
        }
    }

    private void saveEmployee() {
        try {
            employee.setFirstName(firstNameField.getText().trim());
            employee.setLastName(lastNameField.getText().trim());
            employee.setIcPassport(icPassportField.getText().trim());
            employee.setEmail(emailField.getText().trim());
            employee.setPhone(phoneField.getText().trim());
            employee.setDepartment(departmentField.getText().trim());
            employee.setPosition(positionField.getText().trim());

            boolean success;
            if (employee.getEmployeeId() == 0) {
                success = ClientUtils.getEmployeeService().registerEmployee(employee) > 0;
            } else {
                success = ClientUtils.getEmployeeService().updateEmployee(employee);
            }

            if (success) {
                UIUtils.showInfo(this, "Employee saved successfully.");
                if (onSave != null) onSave.run();
                dispose();
            } else {
                UIUtils.showError(this, "Failed to save employee.");
            }
        } catch (Exception e) {
            UIUtils.showError(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
