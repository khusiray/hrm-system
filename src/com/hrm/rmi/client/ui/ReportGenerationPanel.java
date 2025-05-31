// File: com/hrm/rmi/client/ui/ReportGenerationPanel.java
package com.hrm.rmi.client.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.hrm.rmi.client.ClientUtils;
import com.hrm.rmi.common.model.Employee;

public class ReportGenerationPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JComboBox<Employee> employeeCombo;
    private JComboBox<Integer> yearCombo;
    
    private JButton profileReportButton;
    private JButton familyReportButton;
    private JButton leaveReportButton;
    private JButton yearlyReportButton;
    private JButton allEmployeesReportButton;
    
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    
    private List<Employee> employees;
    
    private static final String[] TABLE_COLUMNS = {
        "ID", "First Name", "Last Name", "IC/Passport", "Department", "Position"
    };
    
    public ReportGenerationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        initComponents();
        loadEmployees();
    }
    
    private void initComponents() {
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Report Generation");
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // Report options panel
        JPanel optionsPanel = new JPanel(new BorderLayout(10, 10));
        optionsPanel.setBorder(new TitledBorder("Report Options"));
        
        // Selection panel
        JPanel selectionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        selectionPanel.add(new JLabel("Select Employee:"));
        employeeCombo = new JComboBox<>();
        selectionPanel.add(employeeCombo);
        
        selectionPanel.add(new JLabel("Select Year:"));
        yearCombo = new JComboBox<>();
        
        // Add years (current year and past 2 years)
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearCombo.addItem(currentYear);
        yearCombo.addItem(currentYear - 1);
        yearCombo.addItem(currentYear - 2);
        
        selectionPanel.add(yearCombo);
        
        optionsPanel.add(selectionPanel, BorderLayout.NORTH);
        
        // Report buttons panel
        JPanel reportButtonsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        reportButtonsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        profileReportButton = new JButton("Employee Profile Report");
        profileReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateEmployeeProfileReport();
            }
        });
        
        familyReportButton = new JButton("Family Details Report");
        familyReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateFamilyDetailsReport();
            }
        });
        
        leaveReportButton = new JButton("Leave History Report");
        leaveReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateLeaveHistoryReport();
            }
        });
        
        yearlyReportButton = new JButton("Employee Yearly Report");
        yearlyReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateEmployeeYearlyReport();
            }
        });
        
        allEmployeesReportButton = new JButton("All Employees Report");
        allEmployeesReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateAllEmployeesReport();
            }
        });
        
        reportButtonsPanel.add(profileReportButton);
        reportButtonsPanel.add(familyReportButton);
        reportButtonsPanel.add(leaveReportButton);
        reportButtonsPanel.add(yearlyReportButton);
        reportButtonsPanel.add(allEmployeesReportButton);
        
        optionsPanel.add(reportButtonsPanel, BorderLayout.CENTER);
        
        mainPanel.add(optionsPanel, BorderLayout.NORTH);
        
        // Employee table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new TitledBorder("Employees"));
        
        tableModel = new DefaultTableModel(TABLE_COLUMNS, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        employeeTable = new JTable(tableModel);
        employeeTable.getTableHeader().setReorderingAllowed(false);
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && employeeTable.getSelectedRow() != -1) {
                int selectedRow = employeeTable.getSelectedRow();
                selectEmployee(selectedRow);
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button panel
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadEmployees();
            }
        });
        refreshPanel.add(refreshButton);
        tablePanel.add(refreshPanel, BorderLayout.SOUTH);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void loadEmployees() {
        try {
            // Load all employees
            employees = ClientUtils.getEmployeeService().getAllEmployees();
            
            // Clear table and combo box
            tableModel.setRowCount(0);
            employeeCombo.removeAllItems();
            
            // Add data to table and combo box
            for (Employee employee : employees) {
                Object[] rowData = {
                    employee.getEmployeeId(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getIcPassport(),
                    employee.getDepartment() != null ? employee.getDepartment() : "",
                    employee.getPosition() != null ? employee.getPosition() : ""
                };
                tableModel.addRow(rowData);
                employeeCombo.addItem(employee);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void selectEmployee(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < employees.size()) {
            Employee selectedEmployee = employees.get(selectedRow);
            employeeCombo.setSelectedItem(selectedEmployee);
        }
    }
    
    private void generateEmployeeProfileReport() {
        try {
            Employee selectedEmployee = (Employee) employeeCombo.getSelectedItem();
            
            if (selectedEmployee == null) {
                JOptionPane.showMessageDialog(this, "Please select an employee.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            byte[] reportData = ClientUtils.getReportService().generateEmployeeProfileReport(
                    selectedEmployee.getEmployeeId());
            
            if (reportData != null && reportData.length > 0) {
                // Save the report
                UIUtils.savePdfReport(this, reportData, 
                        "Employee_Profile_" + selectedEmployee.getEmployeeId() + ".pdf");
            } else {
                JOptionPane.showMessageDialog(this, "No data available for report.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void generateFamilyDetailsReport() {
        try {
            Employee selectedEmployee = (Employee) employeeCombo.getSelectedItem();
            
            if (selectedEmployee == null) {
                JOptionPane.showMessageDialog(this, "Please select an employee.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            byte[] reportData = ClientUtils.getReportService().generateEmployeeFamilyDetailsReport(
                    selectedEmployee.getEmployeeId());
            
            if (reportData != null && reportData.length > 0) {
                // Save the report
                UIUtils.savePdfReport(this, reportData, 
                        "Family_Details_" + selectedEmployee.getEmployeeId() + ".pdf");
            } else {
                JOptionPane.showMessageDialog(this, "No data available for report.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void generateLeaveHistoryReport() {
        try {
            Employee selectedEmployee = (Employee) employeeCombo.getSelectedItem();
            
            if (selectedEmployee == null) {
                JOptionPane.showMessageDialog(this, "Please select an employee.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int selectedYear = (Integer) yearCombo.getSelectedItem();
            
            byte[] reportData = ClientUtils.getReportService().generateEmployeeLeaveHistoryReport(
                    selectedEmployee.getEmployeeId(), selectedYear);
            
            if (reportData != null && reportData.length > 0) {
                // Save the report
                UIUtils.savePdfReport(this, reportData, 
                        "Leave_History_" + selectedEmployee.getEmployeeId() + "_" + selectedYear + ".pdf");
            } else {
                JOptionPane.showMessageDialog(this, "No data available for report.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void generateEmployeeYearlyReport() {
        try {
            Employee selectedEmployee = (Employee) employeeCombo.getSelectedItem();
            
            if (selectedEmployee == null) {
                JOptionPane.showMessageDialog(this, "Please select an employee.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int selectedYear = (Integer) yearCombo.getSelectedItem();
            
            byte[] reportData = ClientUtils.getReportService().generateEmployeeYearlyReport(
                    selectedEmployee.getEmployeeId(), selectedYear);
            
            if (reportData != null && reportData.length > 0) {
                // Save the report
                UIUtils.savePdfReport(this, reportData, 
                        "Yearly_Report_" + selectedEmployee.getEmployeeId() + "_" + selectedYear + ".pdf");
            } else {
                JOptionPane.showMessageDialog(this, "No data available for report.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void generateAllEmployeesReport() {
        try {
            byte[] reportData = ClientUtils.getReportService().generateAllEmployeesReport();
            
            if (reportData != null && reportData.length > 0) {
                // Save the report
                UIUtils.savePdfReport(this, reportData, "All_Employees_Report.pdf");
            } else {
                JOptionPane.showMessageDialog(this, "No data available for report.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}