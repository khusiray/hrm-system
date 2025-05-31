// File: com/hrm/rmi/common/util/Constants.java
package com.hrm.rmi.common.util;

public class Constants {
    // RMI Service Names
    public static final String SERVICE_NAME = "HRMService";
    public static final int RMI_PORT = 1099;
    
    // User Types
    public static final String USER_TYPE_HR = "HR";
    public static final String USER_TYPE_EMPLOYEE = "EMPLOYEE";
    
    // Leave Status
    public static final String LEAVE_STATUS_PENDING = "PENDING";
    public static final String LEAVE_STATUS_APPROVED = "APPROVED";
    public static final String LEAVE_STATUS_REJECTED = "REJECTED";
    
    // Report Types
    public static final String REPORT_TYPE_PDF = "PDF";
    public static final String REPORT_TYPE_CSV = "CSV";
    
    // Default configuration
    public static final int CURRENT_YEAR = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
}