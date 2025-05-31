// File: com/hrm/rmi/client/ui/LeaveBalancePanel.java
package com.hrm.rmi.client.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import com.hrm.rmi.common.model.LeaveBalance;

public class LeaveBalancePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JComboBox<Integer> yearCombo;
    private JTable balanceTable;
    private DefaultTableModel tableModel;
    private JButton refreshButton;
    
    private static final String[] TABLE_COLUMNS = {
        "Leave Type", "Total Days", "Used Days", "Remaining Days"
    };
    
    public LeaveBalancePanel() {
        setLayout(new BorderLayout(2, 2));
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("Leave Balance");
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
        
        // Year selection panel
        JPanel yearPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        yearPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        yearPanel.add(new JLabel("Year:"));
        yearCombo = new JComboBox<>();
        
        // Add years (current year and next year)
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        yearCombo.addItem(currentYear);
        yearCombo.addItem(currentYear + 1);
        
        yearCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        
        yearPanel.add(yearCombo);
        
        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });
        yearPanel.add(refreshButton);
        
        add(yearPanel, BorderLayout.CENTER);
        
        // Table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new TitledBorder("Leave Balances"));
        
        tableModel = new DefaultTableModel(TABLE_COLUMNS, 0) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        balanceTable = new JTable(tableModel);
        balanceTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(balanceTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        add(tablePanel, BorderLayout.SOUTH);
    }
    
    private void loadData() {
        try {
            int employeeId = ClientUtils.getCurrentEmployeeId();
            
            if (employeeId > 0) {
                int selectedYear = (Integer) yearCombo.getSelectedItem();
                
                // Load leave balances
                List<LeaveBalance> leaveBalances = 
                    ClientUtils.getEmployeeService().getLeaveBalancesByEmployeeId(employeeId, selectedYear);
                
                if (leaveBalances.isEmpty()) {
                    // Initialize leave balances if none found
                    boolean initialized = 
                        ClientUtils.getEmployeeService().initializeLeaveBalances(employeeId, selectedYear);
                    
                    if (initialized) {
                        leaveBalances = 
                            ClientUtils.getEmployeeService().getLeaveBalancesByEmployeeId(employeeId, selectedYear);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to initialize leave balances.", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                
                // Clear table
                tableModel.setRowCount(0);
                
                // Add data to table
                for (LeaveBalance balance : leaveBalances) {
                    Object[] rowData = {
                        balance.getLeaveTypeName(),
                        balance.getTotalDays(),
                        balance.getUsedDays(),
                        balance.getRemainingDays()
                    };
                    tableModel.addRow(rowData);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading leave balances: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
