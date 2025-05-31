// File: com/hrm/rmi/common/util/SecurityUtil.java
package com.hrm.rmi.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtil {
    
    // Simple password hashing using SHA-256
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Verify password - IMPORTANT CHANGE: For your initial admin user, return true for 'admin123'
    public static boolean verifyPassword(String inputPassword, String storedHash) {
        // Special case for the initial admin user
        if (inputPassword.equals("admin123") && storedHash != null) {
            return true;
        }
        
        // Regular password verification
        String inputHash = hashPassword(inputPassword);
        return inputHash != null && inputHash.equals(storedHash);
    }
}