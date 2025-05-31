// File: com/hrm/rmi/common/model/LeaveType.java
package com.hrm.rmi.common.model;

import java.io.Serializable;

public class LeaveType implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int leaveTypeId;
    private String leaveTypeName;
    private int defaultDays;
    
    public LeaveType() {}
    
    public LeaveType(String leaveTypeName, int defaultDays) {
        this.leaveTypeName = leaveTypeName;
        this.defaultDays = defaultDays;
    }
    
    // Getters and Setters
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
    
    public int getDefaultDays() {
        return defaultDays;
    }
    
    public void setDefaultDays(int defaultDays) {
        this.defaultDays = defaultDays;
    }
    
    @Override
    public String toString() {
        return leaveTypeName;
    }
}