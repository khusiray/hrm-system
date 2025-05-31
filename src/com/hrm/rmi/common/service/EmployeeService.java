// File: com/hrm/rmi/common/service/EmployeeService.java
package com.hrm.rmi.common.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.hrm.rmi.common.model.Employee;
import com.hrm.rmi.common.model.FamilyDetail;
import com.hrm.rmi.common.model.LeaveApplication;
import com.hrm.rmi.common.model.LeaveBalance;
import com.hrm.rmi.common.model.LeaveType;

public interface EmployeeService extends Remote {
    // Employee management
    int registerEmployee(Employee employee) throws RemoteException;
    Employee getEmployeeById(int id) throws RemoteException;
    Employee getEmployeeByIcPassport(String icPassport) throws RemoteException;
    List<Employee> getAllEmployees() throws RemoteException;
    boolean updateEmployee(Employee employee) throws RemoteException;
    
    // Family details
    boolean addFamilyDetail(FamilyDetail familyDetail) throws RemoteException;
    FamilyDetail getFamilyDetailById(int familyId) throws RemoteException;
    List<FamilyDetail> getFamilyDetailsByEmployeeId(int employeeId) throws RemoteException;
    boolean updateFamilyDetail(FamilyDetail familyDetail) throws RemoteException;
    boolean deleteFamilyDetail(int familyId) throws RemoteException;
    
    // Leave management
    List<LeaveType> getAllLeaveTypes() throws RemoteException;
    LeaveType getLeaveTypeById(int leaveTypeId) throws RemoteException;
    
    boolean applyForLeave(LeaveApplication leave) throws RemoteException;
    LeaveApplication getLeaveApplicationById(int leaveId) throws RemoteException;
    List<LeaveApplication> getLeaveApplicationsByEmployeeId(int employeeId) throws RemoteException;
    List<LeaveApplication> getAllLeaveApplications() throws RemoteException;
    boolean updateLeaveStatus(int leaveId, String status) throws RemoteException;
    
    // Leave balance
    LeaveBalance getLeaveBalance(int employeeId, int leaveTypeId, int year) throws RemoteException;
    List<LeaveBalance> getLeaveBalancesByEmployeeId(int employeeId, int year) throws RemoteException;
    boolean initializeLeaveBalances(int employeeId, int year) throws RemoteException;
    boolean updateLeaveBalance(LeaveBalance leaveBalance) throws RemoteException;
}