// File: com/hrm/rmi/client/ui/PayrollManagementPanel.java
package com.hrm.rmi.client.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.hrm.rmi.client.ClientUtils;
import com.hrm.rmi.common.model.Employee;
import com.hrm.rmi.common.model.Salary;

public class PayrollManagementPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JComboBox<Employee> employeeCombo;
    private JComboBox<String> monthCombo;
    private JComboBox<Integer> yearCombo;
    private JTextField basicSalaryField;
    private JTextField allowancesField;
    private JTextField deductionsField;
    private JTextField netSalaryField;
    
    private JTable salaryTable;
    private DefaultTableModel tableModel;
    
    private JButton processButton;
    private JButton processAllButton;
    private JButton paySlipButton;
    private JButton reportButton;
    private JButton refreshButton;
    
    private List<Employee> employees;
    private List<Salary> salaries;
    
    private static final String[] MONTHS = {
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    };
    
    private static final String[] TABLE_COLUMNS = {
        "ID", "Employee", "Month", "Year", "Basic Salary", "Allowances", "Deductions", "Net Salary", "Status"
    };
    
    public PayrollManagementPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Payroll Management");
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Main panel (split into top and bottom sections)
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // Top section (salary processing form)
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        // Form panel
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
formPanel.setBorder(new TitledBorder("Process Salary"));
        
        // Form fields
        JPanel fieldsPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        
        fieldsPanel.add(new JLabel("Select Employee:"));
        employeeCombo = new JComboBox<>();
        fieldsPanel.add(employeeCombo);
        
        fieldsPanel.add(new JLabel("Month:"));
        monthCombo = new JComboBox<>(MONTHS);
        // Set current month
        Calendar cal = Calendar.getInstance();
        monthCombo.setSelectedIndex(cal.get(Calendar.MONTH));
        fieldsPanel.add(monthCombo);
        
        fieldsPanel.add(new JLabel("Year:"));
        yearCombo = new JComboBox<>();
        int currentYear = cal.get(Calendar.YEAR);
        yearCombo.addItem(currentYear - 1);
        yearCombo.addItem(currentYear);
        yearCombo.addItem(currentYear + 1);
        yearCombo.setSelectedItem(currentYear);
        fieldsPanel.add(yearCombo);
        
        fieldsPanel.add(new JLabel("Basic Salary:"));
        basicSalaryField = new JTextField("5000.00"); // Default value
        fieldsPanel.add(basicSalaryField);
        
        fieldsPanel.add(new JLabel("Allowances:"));
        allowancesField = new JTextField("1000.00"); // Default value
        fieldsPanel.add(allowancesField);
        
        fieldsPanel.add(new JLabel("Deductions:"));
        deductionsField = new JTextField("500.00"); // Default value
        fieldsPanel.add(deductionsField);
        
        fieldsPanel.add(new JLabel("Net Salary:"));
        netSalaryField = new JTextField();
        netSalaryField.setEditable(false);
        fieldsPanel.add(netSalaryField);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton calculateButton = new JButton("Calculate Net");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateNetSalary();
            }
        });
        
        processButton = new JButton("Process Salary");
        processButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processSalary();
            }
        });
        
        processAllButton = new JButton("Process All");
        processAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processAllSalaries();
            }
        });
        
        buttonPanel.add(calculateButton);
        buttonPanel.add(processButton);
        buttonPanel.add(processAllButton);
        
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        topPanel.add(formPanel, BorderLayout.CENTER);
        
        // Bottom section (salary table and report generation)
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(new TitledBorder("Salary Records"));
        
        tableModel = new DefaultTableModel(TABLE_COLUMNS, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        salaryTable = new JTable(tableModel);
        salaryTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(salaryTable);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Actions panel
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        paySlipButton = new JButton("Generate Pay Slip");
        paySlipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generatePaySlip();
            }
        });
        
        reportButton = new JButton("Generate Report");
        reportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateReport();
            }
        });
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        
        actionsPanel.add(paySlipButton);
        actionsPanel.add(reportButton);
        actionsPanel.add(refreshButton);
        
        bottomPanel.add(actionsPanel, BorderLayout.SOUTH);
        
        // Layout main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(bottomPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void loadData() {
        try {
            // Load all employees
            employees = ClientUtils.getEmployeeService().getAllEmployees();
            
            // Clear combo box
            employeeCombo.removeAllItems();
            
            // Add employees to combo box
            for (Employee employee : employees) {
                employeeCombo.addItem(employee);
            }
            
            // Load salaries for current month and year
            String selectedMonth = (String) monthCombo.getSelectedItem();
            int selectedYear = (Integer) yearCombo.getSelectedItem();
            
            salaries = ClientUtils.getPayrollService().getSalariesByMonth(selectedMonth, selectedYear);
            
            // Clear table
            tableModel.setRowCount(0);
            
            // Add data to table
            for (Salary salary : salaries) {
                // Find employee name
                String employeeName = "Unknown";
                for (Employee emp : employees) {
                    if (emp.getEmployeeId() == salary.getEmployeeId()) {
                        employeeName = emp.getFirstName() + " " + emp.getLastName();
                        break;
                    }
                }
                
                Object[] rowData = {
                    salary.getSalaryId(),
                    employeeName,
                    salary.getPaymentMonth(),
                    salary.getPaymentYear(),
                    String.format("$%.2f", salary.getBasicSalary()),
                    String.format("$%.2f", salary.getAllowances()),
                    String.format("$%.2f", salary.getDeductions()),
                    String.format("$%.2f", salary.getNetSalary()),
                    salary.getPaymentStatus()
                };
                tableModel.addRow(rowData);
            }
            
            // Clear form fields
            basicSalaryField.setText("5000.00");
            allowancesField.setText("1000.00");
            deductionsField.setText("500.00");
            netSalaryField.setText("");
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void calculateNetSalary() {
        try {
            double basicSalary = Double.parseDouble(basicSalaryField.getText().trim());
            double allowances = Double.parseDouble(allowancesField.getText().trim());
            double deductions = Double.parseDouble(deductionsField.getText().trim());
            
            double netSalary = basicSalary + allowances - deductions;
            
            netSalaryField.setText(String.format("$%.2f", netSalary));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numerical values for salary components.", 
                    "Validation Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void processSalary() {
        try {
            Employee selectedEmployee = (Employee) employeeCombo.getSelectedItem();
            
            if (selectedEmployee == null) {
                JOptionPane.showMessageDialog(this, "Please select an employee.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Get form values
            String selectedMonth = (String) monthCombo.getSelectedItem();
            int selectedYear = (Integer) yearCombo.getSelectedItem();
            
            // Create salary object
            Salary salary = new Salary();
            salary.setEmployeeId(selectedEmployee.getEmployeeId());
            
            try {
                salary.setBasicSalary(Double.parseDouble(basicSalaryField.getText().trim()));
                salary.setAllowances(Double.parseDouble(allowancesField.getText().trim()));
                salary.setDeductions(Double.parseDouble(deductionsField.getText().trim()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter valid numerical values for salary components.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            salary.setNetSalary(salary.calculateNetSalary());
            salary.setPaymentMonth(selectedMonth);
            salary.setPaymentYear(selectedYear);
            salary.setPaymentDate(new java.util.Date());
            salary.setPaymentStatus("Processed");
            
            // Check if salary already exists for this employee/month/year
            boolean exists = false;
            for (Salary existingSalary : salaries) {
                if (existingSalary.getEmployeeId() == selectedEmployee.getEmployeeId()) {
                    exists = true;
                    break;
                }
            }
            
            if (exists) {
                int confirm = JOptionPane.showConfirmDialog(this, 
                        "Salary already processed for this employee in the selected month. Process again?", 
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            // Process salary
            boolean success = ClientUtils.getPayrollService().addSalary(salary);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Salary processed successfully.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to process salary.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error processing salary: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void processAllSalaries() {
        try {
            String selectedMonth = (String) monthCombo.getSelectedItem();
            int selectedYear = (Integer) yearCombo.getSelectedItem();
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "This will process salaries for all employees for " + selectedMonth + " " + selectedYear + ".\nContinue?", 
                    "Confirmation", JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            boolean success = ClientUtils.getPayrollService().processAllSalaries(selectedMonth, selectedYear);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "All salaries processed successfully.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Some salaries failed to process. Please check the log.", 
                        "Partial Success", JOptionPane.WARNING_MESSAGE);
                loadData();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error processing salaries: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void generatePaySlip() {
        try {
            int selectedRow = salaryTable.getSelectedRow();
            
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a salary record.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int salaryId = (Integer) tableModel.getValueAt(selectedRow, 0);
            
            byte[] pdfData = ClientUtils.getPayrollService().generatePaySlip(salaryId);
            
            if (pdfData != null && pdfData.length > 0) {
                String employeeName = (String) tableModel.getValueAt(selectedRow, 1);
                String month = (String) tableModel.getValueAt(selectedRow, 2);
                int year = (Integer) tableModel.getValueAt(selectedRow, 3);
                
                // Save pay slip
                UIUtils.savePdfReport(this, pdfData, 
                        "PaySlip_" + employeeName.replace(" ", "_") + "_" + month + "_" + year + ".pdf");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to generate pay slip.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating pay slip: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void generateReport() {
        try {
            String selectedMonth = (String) monthCombo.getSelectedItem();
            int selectedYear = (Integer) yearCombo.getSelectedItem();
            
            byte[] pdfData = ClientUtils.getPayrollService().generatePayrollReport(selectedMonth, selectedYear);
            
            if (pdfData != null && pdfData.length > 0) {
                // Save report
                UIUtils.savePdfReport(this, pdfData, 
                        "PayrollReport_" + selectedMonth + "_" + selectedYear + ".pdf");
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