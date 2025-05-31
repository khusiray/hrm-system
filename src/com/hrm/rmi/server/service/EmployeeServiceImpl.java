// File: com/hrm/rmi/server/service/EmployeeServiceImpl.java
package com.hrm.rmi.server.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import com.hrm.rmi.common.model.Employee;
import com.hrm.rmi.common.model.FamilyDetail;
import com.hrm.rmi.common.model.LeaveApplication;
import com.hrm.rmi.common.model.LeaveBalance;
import com.hrm.rmi.common.model.LeaveType;
import com.hrm.rmi.common.service.EmployeeService;
import com.hrm.rmi.server.dao.EmployeeDAO;
import com.hrm.rmi.server.dao.FamilyDetailDAO;
import com.hrm.rmi.server.dao.LeaveApplicationDAO;
import com.hrm.rmi.server.dao.LeaveBalanceDAO;
import com.hrm.rmi.server.dao.LeaveTypeDAO;

public class EmployeeServiceImpl extends UnicastRemoteObject implements EmployeeService {
    private static final long serialVersionUID = 1L;
    
    private EmployeeDAO employeeDAO;
    private FamilyDetailDAO familyDetailDAO;
    private LeaveApplicationDAO leaveApplicationDAO;
    private LeaveBalanceDAO leaveBalanceDAO;
    private LeaveTypeDAO leaveTypeDAO;
    
    public EmployeeServiceImpl() throws RemoteException {
        super();
        this.employeeDAO = new EmployeeDAO();
        this.familyDetailDAO = new FamilyDetailDAO();
        this.leaveApplicationDAO = new LeaveApplicationDAO();
        this.leaveBalanceDAO = new LeaveBalanceDAO();
        this.leaveTypeDAO = new LeaveTypeDAO();
    }
    
    @Override
    public int registerEmployee(Employee employee) throws RemoteException {
        try {
            return employeeDAO.addEmployee(employee);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error registering employee", e);
        }
    }
    
    @Override
    public Employee getEmployeeById(int id) throws RemoteException {
        try {
            return employeeDAO.getEmployeeById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting employee by ID", e);
        }
    }
    
    @Override
    public Employee getEmployeeByIcPassport(String icPassport) throws RemoteException {
        try {
            return employeeDAO.getEmployeeByIcPassport(icPassport);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting employee by IC/Passport", e);
        }
    }
    
    @Override
    public List<Employee> getAllEmployees() throws RemoteException {
        try {
            return employeeDAO.getAllEmployees();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting all employees", e);
        }
    }
    
    @Override
    public boolean updateEmployee(Employee employee) throws RemoteException {
        try {
            return employeeDAO.updateEmployee(employee);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error updating employee", e);
        }
    }
    
    @Override
    public boolean addFamilyDetail(FamilyDetail familyDetail) throws RemoteException {
        try {
            return familyDetailDAO.addFamilyDetail(familyDetail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error adding family detail", e);
        }
    }
    
    @Override
    public FamilyDetail getFamilyDetailById(int familyId) throws RemoteException {
        try {
            return familyDetailDAO.getFamilyDetailById(familyId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting family detail by ID", e);
        }
    }
    
    @Override
    public List<FamilyDetail> getFamilyDetailsByEmployeeId(int employeeId) throws RemoteException {
        try {
            return familyDetailDAO.getFamilyDetailsByEmployeeId(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting family details by employee ID", e);
        }
    }
    
    @Override
    public boolean updateFamilyDetail(FamilyDetail familyDetail) throws RemoteException {
        try {
            return familyDetailDAO.updateFamilyDetail(familyDetail);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error updating family detail", e);
        }
    }
    
    @Override
    public boolean deleteFamilyDetail(int familyId) throws RemoteException {
        try {
            return familyDetailDAO.deleteFamilyDetail(familyId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error deleting family detail", e);
        }
    }
    
    @Override
    public List<LeaveType> getAllLeaveTypes() throws RemoteException {
        try {
            return leaveTypeDAO.getAllLeaveTypes();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting all leave types", e);
        }
    }
    
    @Override
    public LeaveType getLeaveTypeById(int leaveTypeId) throws RemoteException {
        try {
            return leaveTypeDAO.getLeaveTypeById(leaveTypeId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting leave type by ID", e);
        }
    }
    
    @Override
    public boolean applyForLeave(LeaveApplication leave) throws RemoteException {
        try {
            return leaveApplicationDAO.applyForLeave(leave);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error applying for leave", e);
        }
    }
    
    @Override
    public LeaveApplication getLeaveApplicationById(int leaveId) throws RemoteException {
        try {
            return leaveApplicationDAO.getLeaveApplicationById(leaveId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting leave application by ID", e);
        }
    }
    
    @Override
    public List<LeaveApplication> getLeaveApplicationsByEmployeeId(int employeeId) throws RemoteException {
        try {
            return leaveApplicationDAO.getLeaveApplicationsByEmployeeId(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting leave applications by employee ID", e);
        }
    }
    
    @Override
    public List<LeaveApplication> getAllLeaveApplications() throws RemoteException {
        try {
            return leaveApplicationDAO.getAllLeaveApplications();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting all leave applications", e);
        }
    }
    
    @Override
    public boolean updateLeaveStatus(int leaveId, String status) throws RemoteException {
        try {
            LeaveApplication leave = leaveApplicationDAO.getLeaveApplicationById(leaveId);
            if (leave == null) {
                return false;
            }
            
            // If status is changing to APPROVED, update leave balance
            if ("APPROVED".equals(status) && !"APPROVED".equals(leave.getStatus())) {
                // Get the year from the leave start date
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(leave.getStartDate());
                int year = cal.get(java.util.Calendar.YEAR);
                
                // Update the leave balance
                leaveBalanceDAO.updateLeaveBalanceOnLeaveApproval(
                    leave.getEmployeeId(), 
                    leave.getLeaveTypeId(),
                    year,
                    leave.getTotalDays()
                );
            }
            
            return leaveApplicationDAO.updateLeaveStatus(leaveId, status);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error updating leave status", e);
        }
    }
    
    @Override
    public LeaveBalance getLeaveBalance(int employeeId, int leaveTypeId, int year) throws RemoteException {
        try {
            return leaveBalanceDAO.getLeaveBalance(employeeId, leaveTypeId, year);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting leave balance", e);
        }
    }
    
    @Override
    public List<LeaveBalance> getLeaveBalancesByEmployeeId(int employeeId, int year) throws RemoteException {
        try {
            return leaveBalanceDAO.getLeaveBalancesByEmployeeId(employeeId, year);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting leave balances by employee ID", e);
        }
    }
    
    @Override
    public boolean initializeLeaveBalances(int employeeId, int year) throws RemoteException {
        try {
            return leaveBalanceDAO.initializeLeaveBalances(employeeId, year);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error initializing leave balances", e);
        }
    }
    
    @Override
    public boolean updateLeaveBalance(LeaveBalance leaveBalance) throws RemoteException {
        try {
            return leaveBalanceDAO.updateLeaveBalance(leaveBalance);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error updating leave balance", e);
        }
    }
}
    