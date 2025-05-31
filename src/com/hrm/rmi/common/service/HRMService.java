// File: com/hrm/rmi/common/service/HRMService.java
package com.hrm.rmi.common.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HRMService extends Remote {
    AuthenticationService getAuthenticationService() throws RemoteException;
    EmployeeService getEmployeeService() throws RemoteException;
    ReportService getReportService() throws RemoteException;
    PayrollService getPayrollService() throws RemoteException; // Add this line
    String ping() throws RemoteException;
}
