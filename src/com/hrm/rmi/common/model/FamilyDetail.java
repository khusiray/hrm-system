// File: com/hrm/rmi/common/model/FamilyDetail.java
package com.hrm.rmi.common.model;

import java.io.Serializable;
import java.util.Date;

public class FamilyDetail implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int familyId;
    private int employeeId;
    private String relationType;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private Date createdAt;
    private Date updatedAt;
    
    public FamilyDetail() {}
    
    public FamilyDetail(int employeeId, String relationType, String firstName, String lastName) {
        this.employeeId = employeeId;
        this.relationType = relationType;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Getters and Setters
    public int getFamilyId() {
        return familyId;
    }
    
    public void setFamilyId(int familyId) {
        this.familyId = familyId;
    }
    
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getRelationType() {
        return relationType;
    }
    
    public void setRelationType(String relationType) {
        this.relationType = relationType;
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
    
    public String getContactNumber() {
        return contactNumber;
    }
    
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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
        return relationType + ": " + firstName + " " + lastName;
    }
}