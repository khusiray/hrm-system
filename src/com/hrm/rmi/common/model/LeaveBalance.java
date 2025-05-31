// File: com/hrm/rmi/common/model/LeaveBalance.java
package com.hrm.rmi.common.model;

import java.io.Serializable;
import java.util.Date;

public class LeaveBalance implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int balanceId;
    private int employeeId;
    private int leaveTypeId;
    private String leaveTypeName; // Not stored in DB, for display purposes
    private int year;
    private int totalDays;
    private int usedDays;
    private Date createdAt;
    private Date updatedAt;
    
    public LeaveBalance() {}
    
    public LeaveBalance(int employeeId, int leaveTypeId, int year, int totalDays) {
        this.employeeId = employeeId;
        this.leaveTypeId = leaveTypeId;
        this.year = year;
        this.totalDays = totalDays;
        this.usedDays = 0;
    }
    
    // Getters and Setters
    public int getBalanceId() {
        return balanceId;
    }
    
    public void setBalanceId(int balanceId) {
        this.balanceId = balanceId;
    }
    
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public int getLeaveTypeId() {
        return leaveTypeId;
    }
    
    public void setLeaveTypeId(int leaveTypeId) {
        this.leaveTypeId = leaveTypeId;
    }
    
    public String getLeaveTypeName() {
        return leaveTypeName;
    }
    
    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public int getTotalDays() {
        return totalDays;
    }
    
    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }
    
    public int getUsedDays() {
        return usedDays;
    }
    
    public void setUsedDays(int usedDays) {
        this.usedDays = usedDays;
    }
    
    public int getRemainingDays() {
        return totalDays - usedDays;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}