// File: com/hrm/rmi/common/service/ReportService.java
package com.hrm.rmi.common.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ReportService extends Remote {
    byte[] generateEmployeeProfileReport(int employeeId) throws RemoteException;
    byte[] generateEmployeeFamilyDetailsReport(int employeeId) throws RemoteException;
    byte[] generateEmployeeLeaveHistoryReport(int employeeId, int year) throws RemoteException;
    byte[] generateEmployeeYearlyReport(int employeeId, int year) throws RemoteException;
    byte[] generateAllEmployeesReport() throws RemoteException;
}
