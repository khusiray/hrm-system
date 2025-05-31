// File: com/hrm/rmi/server/service/ReportServiceImpl.java
package com.hrm.rmi.server.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.hrm.rmi.common.service.ReportService;
import com.hrm.rmi.server.util.ReportGenerator;

public class ReportServiceImpl extends UnicastRemoteObject implements ReportService {
    private static final long serialVersionUID = 1L;
    
    private ReportGenerator reportGenerator;
    
    public ReportServiceImpl() throws RemoteException {
        super();
        this.reportGenerator = new ReportGenerator();
    }
    
    @Override
    public byte[] generateEmployeeProfileReport(int employeeId) throws RemoteException {
        try {
            return reportGenerator.generateEmployeeProfileReport(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error generating employee profile report", e);
        }
    }
    
    @Override
    public byte[] generateEmployeeFamilyDetailsReport(int employeeId) throws RemoteException {
        try {
            return reportGenerator.generateEmployeeFamilyDetailsReport(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error generating family details report", e);
        }
    }
    
    @Override
    public byte[] generateEmployeeLeaveHistoryReport(int employeeId, int year) throws RemoteException {
        try {
            return reportGenerator.generateEmployeeLeaveHistoryReport(employeeId, year);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error generating leave history report", e);
        }
    }
    
    @Override
    public byte[] generateEmployeeYearlyReport(int employeeId, int year) throws RemoteException {
        try {
            return reportGenerator.generateEmployeeYearlyReport(employeeId, year);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error generating yearly report", e);
        }
    }
    
    @Override
    public byte[] generateAllEmployeesReport() throws RemoteException {
        try {
            return reportGenerator.generateAllEmployeesReport();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error generating all employees report", e);
        }
    }
}