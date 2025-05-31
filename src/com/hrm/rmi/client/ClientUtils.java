// File: com/hrm/rmi/client/ClientUtils.java
package com.hrm.rmi.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.hrm.rmi.common.model.User;
import com.hrm.rmi.common.service.*;
import com.hrm.rmi.common.util.Constants;

public class ClientUtils {
    private static HRMService hrmService;
    private static AuthenticationService authService;
    private static EmployeeService employeeService;
    private static ReportService reportService;
    private static PayrollService payrollService; // Add this line
    
    private static User currentUser;
    
    public static void connect(String host, int port) throws Exception {
        // Look up the remote object
        Registry registry = LocateRegistry.getRegistry(host, port);
        hrmService = (HRMService) registry.lookup(Constants.SERVICE_NAME);
        
        // Get the service interfaces
        authService = hrmService.getAuthenticationService();
        employeeService = hrmService.getEmployeeService();
        reportService = hrmService.getReportService();
        payrollService = hrmService.getPayrollService(); // Add this line
        
        // Test connection
        String ping = hrmService.ping();
        System.out.println("Connected to server: " + ping);
    }
    
    // Add getter for PayrollService
    public static PayrollService getPayrollService() {
        return payrollService;
    }
    
    // Existing code...
    public static HRMService getHRMService() {
        return hrmService;
    }
    
    public static AuthenticationService getAuthService() {
        return authService;
    }
    
    public static EmployeeService getEmployeeService() {
        return employeeService;
    }
    
    public static ReportService getReportService() {
        return reportService;
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public static boolean isHR() {
        return isLoggedIn() && currentUser.isHR();
    }
    
    public static boolean isEmployee() {
        return isLoggedIn() && currentUser.isEmployee();
    }
    
    public static void logout() {
        currentUser = null;
    }
    
    public static int getCurrentEmployeeId() {
        if (isLoggedIn() && currentUser.getEmployeeId() != null) {
            return currentUser.getEmployeeId();
        }
        return -1;
    }
}