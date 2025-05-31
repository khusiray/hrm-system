// File: com/hrm/rmi/common/model/Salary.java
package com.hrm.rmi.common.model;

import java.io.Serializable;
import java.util.Date;

public class Salary implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int salaryId;
    private int employeeId;
    private double basicSalary;
    private double allowances;
    private double deductions;
    private double netSalary;
    private String paymentMonth;
    private int paymentYear;
    private Date paymentDate;
    private String paymentStatus;
    private Date createdAt;
    private Date updatedAt;
    
    public Salary() {}
    
    // Getters and Setters
    public int getSalaryId() {
        return salaryId;
    }
    
    public void setSalaryId(int salaryId) {
        this.salaryId = salaryId;
    }
    
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public double getBasicSalary() {
        return basicSalary;
    }
    
    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }
    
    public double getAllowances() {
        return allowances;
    }
    
    public void setAllowances(double allowances) {
        this.allowances = allowances;
    }
    
    public double getDeductions() {
        return deductions;
    }
    
    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }
    
    public double getNetSalary() {
        return netSalary;
    }
    
    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }
    
    public String getPaymentMonth() {
        return paymentMonth;
    }
    
    public void setPaymentMonth(String paymentMonth) {
        this.paymentMonth = paymentMonth;
    }
    
    public int getPaymentYear() {
        return paymentYear;
    }
    
    public void setPaymentYear(int paymentYear) {
        this.paymentYear = paymentYear;
    }
    
    public Date getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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
    
    public double calculateNetSalary() {
        return basicSalary + allowances - deductions;
    }
}