// File: com/hrm/rmi/client/ClientMain.java
package com.hrm.rmi.client;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class ClientMain {
    
    public static void main(String[] args) {
        try {
            // Set look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Connect to server
            String host = "localhost"; // Default host
            int port = 1099; // Default port
            
            // Allow custom server settings
            if (args.length >= 1) {
                host = args[0];
            }
            if (args.length >= 2) {
                try {
                    port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid port number, using default: 1099");
                }
            }
            
            // Try to connect to server
            try {
                ClientUtils.connect(host, port);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                        "Error connecting to server: " + e.getMessage() + "\n" +
                        "Please make sure the server is running and try again.",
                        "Connection Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
            
            // Start the login screen
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    LoginFrame loginFrame = new LoginFrame();
                    loginFrame.setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                    "Error starting application: " + e.getMessage(),
                    "Application Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}