// File: com/hrm/rmi/client/ui/UIUtils.java
package com.hrm.rmi.client.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Utility class for UI-related functionality
 */
public class UIUtils {
    
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * Centers a component on the screen
     */
    public static void centerComponent(Component component) {
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Dimension componentSize = component.getSize();
        
        int x = (screenSize.width - componentSize.width) / 2;
        int y = (screenSize.height - componentSize.height) / 2;
        
        component.setLocation(x, y);
    }
    
    /**
     * Shows an error message dialog
     */
    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Shows an information message dialog
     */
    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Shows a confirmation dialog
     */
    public static boolean showConfirmation(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirmation", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
    
    /**
     * Saves a PDF report to a file
     */
    public static void savePdfReport(Component parent, byte[] reportData, String defaultFileName) {
        if (reportData == null || reportData.length == 0) {
            showError(parent, "No report data available.");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Report");
        fileChooser.setSelectedFile(new File(defaultFileName));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));
        
        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            
            // Add .pdf extension if not present
            if (!file.getName().toLowerCase().endsWith(".pdf")) {
                file = new File(file.getAbsolutePath() + ".pdf");
            }
            
            try (FileOutputStream fos = new FileOutputStream(file);
                 ByteArrayInputStream bais = new ByteArrayInputStream(reportData)) {
                
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bais.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
                
                showInfo(parent, "Report saved successfully to:\n" + file.getAbsolutePath());
            } catch (Exception e) {
                showError(parent, "Error saving report: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Parses a date string into a Date object
     */
    public static Date parseDate(String dateStr) {
        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Formats a Date object into a string
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT.format(date);
    }
    
    /**
     * Calculates the number of days between two dates (inclusive)
     */
    public static int calculateDaysBetween(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return 0;
        }
        
        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();
        long diff = endMillis - startMillis;
        
        return (int) (diff / (24 * 60 * 60 * 1000)) + 1; // Add 1 to include both start and end dates
    }
}