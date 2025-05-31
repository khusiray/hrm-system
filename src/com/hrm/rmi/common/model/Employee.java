// File: com/hrm/rmi/common/model/Employee.java
package com.hrm.rmi.common.model;

import java.io.Serializable;
import java.util.Date;

public class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int employeeId;
    private String firstName;
    private String lastName;
    private String icPassport;
    private String email;
    private String phone;
    private Date dateHired;
    private String department;
    private String position;
    private Date createdAt;
    private Date updatedAt;
    
    public Employee() {}
    
    public Employee(String firstName, String lastName, String icPassport) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.icPassport = icPassport;
    }
    
    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getIcPassport() {
        return icPassport;
    }
    
    public void setIcPassport(String icPassport) {
        this.icPassport = icPassport;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public Date getDateHired() {
        return dateHired;
    }
    
    public void setDateHired(Date dateHired) {
        this.dateHired = dateHired;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
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
    
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}