// File: com/hrm/rmi/client/ui/MainFrame.java
package com.hrm.rmi.client.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.hrm.rmi.client.ClientUtils;
import com.hrm.rmi.client.LoginFrame;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // Card layout panel
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    // Panels
    private EmployeeInfoPanel employeeInfoPanel;
    private FamilyDetailsPanel familyDetailsPanel;
    private LeaveApplicationPanel leaveApplicationPanel;
    private LeaveBalancePanel leaveBalancePanel;
    private EmployeeManagementPanel employeeManagementPanel;
    private ReportGenerationPanel reportGenerationPanel;
    private PayrollManagementPanel payrollManagementPanel;

    
    // Constants for card names
    private static final String EMPLOYEE_INFO = "EMPLOYEE_INFO";
    private static final String FAMILY_DETAILS = "FAMILY_DETAILS";
    private static final String LEAVE_APPLICATION = "LEAVE_APPLICATION";
    private static final String LEAVE_BALANCE = "LEAVE_BALANCE";
    private static final String EMPLOYEE_MANAGEMENT = "EMPLOYEE_MANAGEMENT";
    private static final String REPORT_GENERATION = "REPORT_GENERATION";
    private static final String PAYROLL_MANAGEMENT = "PAYROLL_MANAGEMENT";
    
    
    public MainFrame() {
        setTitle("BHEL HRM System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 600);
        UIUtils.centerComponent(this);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
        
        initComponents();
        setupMenuBar();
    }
    
    private void initComponents() {
        // Set up card layout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        
        // Initialize panels
        if (ClientUtils.isEmployee() || ClientUtils.isHR()) {
            // Common panels for both roles
            employeeInfoPanel = new EmployeeInfoPanel();
            familyDetailsPanel = new FamilyDetailsPanel();
            leaveApplicationPanel = new LeaveApplicationPanel();
            leaveBalancePanel = new LeaveBalancePanel();
            
            contentPanel.add(employeeInfoPanel, EMPLOYEE_INFO);
            contentPanel.add(familyDetailsPanel, FAMILY_DETAILS);
            contentPanel.add(leaveApplicationPanel, LEAVE_APPLICATION);
            contentPanel.add(leaveBalancePanel, LEAVE_BALANCE);
        }
        
        // HR-specific panels
        if (ClientUtils.isHR()) {
            employeeManagementPanel = new EmployeeManagementPanel();
            reportGenerationPanel = new ReportGenerationPanel();
            payrollManagementPanel = new PayrollManagementPanel(); // Add this line
            
            contentPanel.add(employeeManagementPanel, EMPLOYEE_MANAGEMENT);
            contentPanel.add(reportGenerationPanel, REPORT_GENERATION);
            contentPanel.add(payrollManagementPanel, PAYROLL_MANAGEMENT); // Add this line
        }
        
        // Show default panel
        if (ClientUtils.isHR()) {
            cardLayout.show(contentPanel, EMPLOYEE_MANAGEMENT);
        } else {
            cardLayout.show(contentPanel, EMPLOYEE_INFO);
        }
        
        // Add to frame
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmExit();
            }
        });
        
        fileMenu.add(logoutItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
     // Employee menu (visible to both HR and employees)
        if (ClientUtils.isEmployee() || ClientUtils.isHR()) {
            JMenu employeeMenu = new JMenu("Employee");
            
            JMenuItem profileItem = new JMenuItem("My Profile");
            profileItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, EMPLOYEE_INFO);
                }
            });
            
            JMenuItem familyItem = new JMenuItem("Family Details");
            familyItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, FAMILY_DETAILS);
                }
            });
            
            employeeMenu.add(profileItem);
            employeeMenu.add(familyItem);
            
            menuBar.add(employeeMenu);
        }
        
        // Leave menu (visible to both HR and employees)
        if (ClientUtils.isEmployee() || ClientUtils.isHR()) {
            JMenu leaveMenu = new JMenu("Leave");
            
            JMenuItem applyItem = new JMenuItem("Apply for Leave");
            applyItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, LEAVE_APPLICATION);
                }
            });
            
            JMenuItem balanceItem = new JMenuItem("Leave Balance");
            balanceItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, LEAVE_BALANCE);
                }
            });
            
            leaveMenu.add(applyItem);
            leaveMenu.add(balanceItem);
            
            menuBar.add(leaveMenu);
        }
        
        // HR menu (visible only to HR staff)
        if (ClientUtils.isHR()) {
            JMenu hrMenu = new JMenu("HR");
            
            JMenuItem manageItem = new JMenuItem("Employee Management");
            manageItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, EMPLOYEE_MANAGEMENT);
                }
            });
            
            JMenuItem reportItem = new JMenuItem("Reports");
            reportItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, REPORT_GENERATION);
                }
            });
            
            JMenuItem payrollItem = new JMenuItem("Payroll Management");
            payrollItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPanel, PAYROLL_MANAGEMENT);
                }
            });
            
            hrMenu.add(manageItem);
            hrMenu.add(reportItem);
            hrMenu.add(payrollItem);
            
            menuBar.add(hrMenu);
        }
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAboutDialog();
            }
        });
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void confirmExit() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit the application?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    private void logout() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        
        if (response == JOptionPane.YES_OPTION) {
            ClientUtils.logout();
            dispose();
            
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                }
            });
        }
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(
                this,
                "BHEL Human Resource Management System\n" +
                "Version 1.0\n\n" +
                "Developed for Distributed Computer Systems Assignment",
                "About",
                JOptionPane.INFORMATION_MESSAGE);
    }
}