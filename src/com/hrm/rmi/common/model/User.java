// File: com/hrm/rmi/common/model/User.java
package com.hrm.rmi.common.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int userId;
    private String username;
    private String password;
    private String userType; // "EMPLOYEE" or "HR"
    private Integer employeeId; // Can be null for general admin users
    
    public User() {}
    
    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public Integer getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
    
    public boolean isHR() {
        return "HR".equals(userType);
    }
    
    public boolean isEmployee() {
        return "EMPLOYEE".equals(userType);
    }
}