// File: com/hrm/rmi/client/LoginFrame.java
package com.hrm.rmi.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.hrm.rmi.client.ui.MainFrame;
import com.hrm.rmi.client.ui.UIUtils;
import com.hrm.rmi.common.model.User;

public class LoginFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton exitButton;
    
    public LoginFrame() {
        setTitle("BHEL HRM System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setResizable(false);
        UIUtils.centerComponent(this);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title panel
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("BHEL Human Resource Management System");
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Login form panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        
        formPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        formPanel.add(usernameField);
        
        formPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        formPanel.add(passwordField);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                attemptLogin();
            }
        });
        
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        buttonPanel.add(loginButton);
        buttonPanel.add(exitButton);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add to frame
        add(mainPanel);
        
        // Set default button
        getRootPane().setDefaultButton(loginButton);
    }
    
    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            UIUtils.showError(this, "Please enter both username and password.");
            return;
        }
        
        try {
            User user = ClientUtils.getAuthService().login(username, password);
            
            if (user != null) {
                ClientUtils.setCurrentUser(user);
                openMainApplication();
            } else {
                UIUtils.showError(this, "Invalid username or password.");
            }
        } catch (Exception e) {
            UIUtils.showError(this, "Error during login: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void openMainApplication() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
                dispose(); // Close login window
            }
        });
    }
}