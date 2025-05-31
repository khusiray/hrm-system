package com.hrm.rmi.server.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.hrm.rmi.common.service.*;

public class HRMServiceImpl extends UnicastRemoteObject implements HRMService {
    private static final long serialVersionUID = 1L;
    
    private AuthenticationService authService;
    private EmployeeService employeeService;
    private ReportService reportService;
    private PayrollService payrollService; // Add this line
    
    public HRMServiceImpl() throws RemoteException {
        super();
        System.setProperty("java.rmi.server.hostname", "localhost");
        System.setProperty("java.rmi.server.useCodebaseOnly", "false");
        
        this.authService = new AuthenticationServiceImpl();
        this.employeeService = new EmployeeServiceImpl();
        this.reportService = new ReportServiceImpl();
        this.payrollService = new PayrollServiceImpl();
        
        System.out.println("HRM Service initialized with security settings");
    }
    
    @Override
    public AuthenticationService getAuthenticationService() throws RemoteException {
        return authService;
    }
    
    @Override
    public EmployeeService getEmployeeService() throws RemoteException {
        return employeeService;
    }
    
    @Override
    public ReportService getReportService() throws RemoteException {
        return reportService;
    }
    
    @Override
    public PayrollService getPayrollService() throws RemoteException {
        return payrollService;
    }
    
    @Override
    public String ping() throws RemoteException {
        return "HRM Service is running!";
    }
}