// File: com/hrm/rmi/client/ui/LeaveApplicationPanel.java
package com.hrm.rmi.client.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.hrm.rmi.client.ClientUtils;
import com.hrm.rmi.common.model.LeaveApplication;
import com.hrm.rmi.common.model.LeaveBalance;
import com.hrm.rmi.common.model.LeaveType;

public class LeaveApplicationPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JComboBox<LeaveType> leaveTypeCombo;
    private JTextField startDateField;
    private JTextField endDateField;
    private JTextField totalDaysField;
    private JTextArea reasonArea;
    
    private JTable applicationsTable;
    private DefaultTableModel tableModel;
    
    private JButton applyButton;
    private JButton clearButton;
    private JButton refreshButton;
    
    private List<LeaveType> leaveTypes;
    private List<LeaveApplication> leaveApplications;
    
    private static final String[] TABLE_COLUMNS = {
        "ID", "Leave Type", "Start Date", "End Date", "Total Days", "Status"
    };
    
    public LeaveApplicationPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Leave Application");
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Main panel (split into form and table)
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        
        // Form panel
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(new TitledBorder("Apply for Leave"));
        
        // Form fields panel
        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        fieldsPanel.add(new JLabel("Leave Type:"));
        leaveTypeCombo = new JComboBox<>();
        fieldsPanel.add(leaveTypeCombo);
        
        fieldsPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        startDateField = new JTextField();
        fieldsPanel.add(startDateField);
        
        fieldsPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        endDateField = new JTextField();
        fieldsPanel.add(endDateField);
        fieldsPanel.add(new JLabel("Total Days:"));
        totalDaysField = new JTextField();
        totalDaysField.setEditable(false);
        fieldsPanel.add(totalDaysField);
        
        fieldsPanel.add(new JLabel("Reason:"));
        reasonArea = new JTextArea(3, 20);
        JScrollPane reasonScrollPane = new JScrollPane(reasonArea);
        fieldsPanel.add(reasonScrollPane);
        
        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        applyButton = new JButton("Submit Application");
        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyForLeave();
            }
        });
        
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        
        buttonPanel.add(applyButton);
        buttonPanel.add(clearButton);
        
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        formPanel.setPreferredSize(new java.awt.Dimension(500, 300)); // Limit form panel height
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new TitledBorder("My Leave Applications"));
        
        tableModel = new DefaultTableModel(TABLE_COLUMNS, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        applicationsTable = new JTable(tableModel);
        applicationsTable.getTableHeader().setReorderingAllowed(false);
        applicationsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Ensure columns resize
        JScrollPane scrollPane = new JScrollPane(applicationsTable);
        scrollPane.setPreferredSize(new java.awt.Dimension(500, 200)); // Ensure table has enough space
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
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
        tablePanel.add(refreshPanel, BorderLayout.SOUTH);
        
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void loadData() {
        try {
            int employeeId = ClientUtils.getCurrentEmployeeId();
            
            if (employeeId > 0) {
                // Load leave types
                leaveTypes = ClientUtils.getEmployeeService().getAllLeaveTypes();
                leaveTypeCombo.removeAllItems();
                for (LeaveType type : leaveTypes) {
                    leaveTypeCombo.addItem(type);
                }
                
                // Load leave applications
                leaveApplications = ClientUtils.getEmployeeService().getLeaveApplicationsByEmployeeId(employeeId);
                
                // Clear table
                tableModel.setRowCount(0);
                
                // Date formatter
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                
                // Add data to table
                for (LeaveApplication application : leaveApplications) {
                    Object[] rowData = {
                        application.getLeaveId(),
                        application.getLeaveTypeName(),
                        dateFormat.format(application.getStartDate()),
                        dateFormat.format(application.getEndDate()),
                        application.getTotalDays(),
                        application.getStatus()
                    };
                    tableModel.addRow(rowData);
                }
                
                // Clear form
                clearForm();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void calculateTotalDays() {
        try {
            String startDateStr = startDateField.getText().trim();
            String endDateStr = endDateField.getText().trim();
            
            if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both start and end dates.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);
            
            if (endDate.before(startDate)) {
                JOptionPane.showMessageDialog(this, "End date cannot be before start date.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Calculate total days (inclusive of start and end dates)
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(startDate);
            
            Calendar endCal = Calendar.getInstance();
            endCal.setTime(endDate);
            
            int totalDays = 0;
            
            while (!startCal.after(endCal)) {
                int dayOfWeek = startCal.get(Calendar.DAY_OF_WEEK);
                // Skip weekends (Saturday and Sunday)
                if (!(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY)) {
                    totalDays++;
                }
                startCal.add(Calendar.DATE, 1);
            }
            
            totalDaysField.setText(String.valueOf(totalDays));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error calculating days: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void applyForLeave() {
        try {
            int employeeId = ClientUtils.getCurrentEmployeeId();
            
            if (employeeId <= 0) {
                JOptionPane.showMessageDialog(this, "Employee ID not found.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Validate fields
            LeaveType selectedLeaveType = (LeaveType) leaveTypeCombo.getSelectedItem();
            if (selectedLeaveType == null) {
                JOptionPane.showMessageDialog(this, "Please select a leave type.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
         // Inside applyForLeave()
            String startDateStr = startDateField.getText().trim();
            String endDateStr = endDateField.getText().trim();
            String reason = reasonArea.getText().trim();

            if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter start date and end date.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Automatically calculate total days
            calculateTotalDays();
            String totalDaysStr = totalDaysField.getText().trim();

            if (totalDaysStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please calculate total days.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (reason.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please provide a reason for leave.", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Parse dates and total days
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);
            int totalDays = Integer.parseInt(totalDaysStr);
            
            // Check if employee has enough leave balance
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            int year = cal.get(Calendar.YEAR);
            
            LeaveBalance balance = ClientUtils.getEmployeeService().getLeaveBalance(
                    employeeId, selectedLeaveType.getLeaveTypeId(), year);
            
            if (balance == null) {
                // Initialize leave balance if not found
                boolean initialized = ClientUtils.getEmployeeService().initializeLeaveBalances(employeeId, year);
                if (!initialized) {
                    JOptionPane.showMessageDialog(this, "Failed to initialize leave balances.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                balance = ClientUtils.getEmployeeService().getLeaveBalance(
                        employeeId, selectedLeaveType.getLeaveTypeId(), year);
            }
            
            if (balance != null && (balance.getRemainingDays() < totalDays)) {
                JOptionPane.showMessageDialog(this, 
                        "Insufficient leave balance. You have " + balance.getRemainingDays() + 
                        " days remaining for " + selectedLeaveType.getLeaveTypeName() + ".", 
                        "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create leave application
            LeaveApplication application = new LeaveApplication();
            application.setEmployeeId(employeeId);
            application.setLeaveTypeId(selectedLeaveType.getLeaveTypeId());
            application.setStartDate(startDate);
            application.setEndDate(endDate);
            application.setTotalDays(totalDays);
            application.setReason(reason);
            application.setStatus("PENDING");
            
            // Submit application
            boolean success = ClientUtils.getEmployeeService().applyForLeave(application);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Leave application submitted successfully.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh data
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to submit leave application.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error applying for leave: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void clearForm() {
        if (leaveTypeCombo.getItemCount() > 0) {
            leaveTypeCombo.setSelectedIndex(0);
        }
        startDateField.setText("");
        endDateField.setText("");
        totalDaysField.setText("");
        reasonArea.setText("");
    }
}