// File: com/hrm/rmi/common/service/PayrollService.java
package com.hrm.rmi.common.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.hrm.rmi.common.model.Salary;

public interface PayrollService extends Remote {
    // Salary management
    boolean addSalary(Salary salary) throws RemoteException;
    Salary getSalaryById(int salaryId) throws RemoteException;
    List<Salary> getSalariesByEmployeeId(int employeeId) throws RemoteException;
    List<Salary> getSalariesByMonth(String month, int year) throws RemoteException;
    boolean updateSalary(Salary salary) throws RemoteException;
    boolean deleteSalary(int salaryId) throws RemoteException;
    
    // Payroll processing
    boolean processSalary(int employeeId, String month, int year) throws RemoteException;
    boolean processAllSalaries(String month, int year) throws RemoteException;
    
    // Reporting
    byte[] generatePaySlip(int salaryId) throws RemoteException;
    byte[] generatePayrollReport(String month, int year) throws RemoteException;
}