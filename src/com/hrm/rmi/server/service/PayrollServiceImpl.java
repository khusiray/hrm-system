// File: com/hrm/rmi/server/service/PayrollServiceImpl.java
package com.hrm.rmi.server.service;

import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hrm.rmi.common.model.Employee;
import com.hrm.rmi.common.model.Salary;
import com.hrm.rmi.common.service.PayrollService;
import com.hrm.rmi.server.dao.EmployeeDAO;
import com.hrm.rmi.server.dao.SalaryDAO;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PayrollServiceImpl extends UnicastRemoteObject implements PayrollService {
    private static final long serialVersionUID = 1L;
    
    private SalaryDAO salaryDAO;
    private EmployeeDAO employeeDAO;
    
    public PayrollServiceImpl() throws RemoteException {
        super();
        this.salaryDAO = new SalaryDAO();
        this.employeeDAO = new EmployeeDAO();
    }
    
    @Override
    public boolean addSalary(Salary salary) throws RemoteException {
        try {
            // Calculate net salary
            salary.setNetSalary(salary.calculateNetSalary());
            return salaryDAO.addSalary(salary);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error adding salary", e);
        }
    }
    
    @Override
    public Salary getSalaryById(int salaryId) throws RemoteException {
        try {
            return salaryDAO.getSalaryById(salaryId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting salary by ID", e);
        }
    }
    
    @Override
    public List<Salary> getSalariesByEmployeeId(int employeeId) throws RemoteException {
        try {
            return salaryDAO.getSalariesByEmployeeId(employeeId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting salaries by employee ID", e);
        }
    }
    
    @Override
    public List<Salary> getSalariesByMonth(String month, int year) throws RemoteException {
        try {
            return salaryDAO.getSalariesByMonth(month, year);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error getting salaries by month", e);
        }
    }
    
    @Override
    public boolean updateSalary(Salary salary) throws RemoteException {
        try {
            // Calculate net salary
            salary.setNetSalary(salary.calculateNetSalary());
            return salaryDAO.updateSalary(salary);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error updating salary", e);
        }
    }
    
    @Override
    public boolean deleteSalary(int salaryId) throws RemoteException {
        try {
            return salaryDAO.deleteSalary(salaryId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error deleting salary", e);
        }
    }
    
    @Override
    public boolean processSalary(int employeeId, String month, int year) throws RemoteException {
        try {
            // Check if salary already processed
            List<Salary> existingSalaries = salaryDAO.getSalariesByMonth(month, year);
            for (Salary existing : existingSalaries) {
                if (existing.getEmployeeId() == employeeId) {
                    return false; // Already processed
                }
            }
            
            // Get employee details
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            if (employee == null) {
                return false;
            }
            
            // Create new salary record
            Salary salary = new Salary();
            salary.setEmployeeId(employeeId);
            
            // This is where you would calculate the salary components based on employee details
            // For this example, we'll use some default values
            salary.setBasicSalary(5000.0); // Default basic salary
            salary.setAllowances(1000.0);  // Default allowances
            salary.setDeductions(500.0);   // Default deductions
            
            salary.setNetSalary(salary.calculateNetSalary());
            salary.setPaymentMonth(month);
            salary.setPaymentYear(year);
            salary.setPaymentDate(new Date()); // Current date
            salary.setPaymentStatus("Processed");
            
            // Save the salary record
            return salaryDAO.addSalary(salary);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error processing salary", e);
        }
    }
    
    @Override
    public boolean processAllSalaries(String month, int year) throws RemoteException {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            boolean allSuccess = true;
            
            for (Employee employee : employees) {
                boolean success = processSalary(employee.getEmployeeId(), month, year);
                if (!success) {
                    allSuccess = false;
                }
            }
            
            return allSuccess;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error processing all salaries", e);
        }
    }
    
    @Override
    public byte[] generatePaySlip(int salaryId) throws RemoteException {
        try {
            Salary salary = salaryDAO.getSalaryById(salaryId);
            if (salary == null) {
                return null;
            }
            
            Employee employee = employeeDAO.getEmployeeById(salary.getEmployeeId());
            if (employee == null) {
                return null;
            }
            
            // Generate PDF pay slip
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Title
            document.add(new Paragraph("BHEL - PAYSLIP"));
            document.add(new Paragraph(" "));
            
            // Employee details
            document.add(new Paragraph("Employee: " + employee.getFirstName() + " " + employee.getLastName()));
            document.add(new Paragraph("Employee ID: " + employee.getEmployeeId()));
            document.add(new Paragraph("Department: " + employee.getDepartment()));
            document.add(new Paragraph("Position: " + employee.getPosition()));
            document.add(new Paragraph(" "));
            
            // Salary period
            document.add(new Paragraph("Pay Period: " + salary.getPaymentMonth() + " " + salary.getPaymentYear()));
            document.add(new Paragraph("Payment Date: " + new SimpleDateFormat("dd-MM-yyyy").format(salary.getPaymentDate())));
            document.add(new Paragraph(" "));
            
            // Salary details
            PdfPTable table = new PdfPTable(2);
            table.addCell("Description");
            table.addCell("Amount");
            
            table.addCell("Basic Salary");
            table.addCell(String.format("$%.2f", salary.getBasicSalary()));
            
            table.addCell("Allowances");
            table.addCell(String.format("$%.2f", salary.getAllowances()));
            
            table.addCell("Deductions");
            table.addCell(String.format("$%.2f", salary.getDeductions()));
            
            table.addCell("Net Salary");
            table.addCell(String.format("$%.2f", salary.getNetSalary()));
            
            document.add(table);
            
            document.close();
            
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error generating pay slip", e);
        }
    }
    
    @Override
    public byte[] generatePayrollReport(String month, int year) throws RemoteException {
        try {
            List<Salary> salaries = salaryDAO.getSalariesByMonth(month, year);
            
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Title
            document.add(new Paragraph("BHEL - PAYROLL REPORT"));
            document.add(new Paragraph("Month: " + month + " " + year));
            document.add(new Paragraph(" "));
            
            // Payroll summary
            double totalBasic = 0;
            double totalAllowances = 0;
            double totalDeductions = 0;
            double totalNet = 0;
            
            // Payroll details
            PdfPTable table = new PdfPTable(6);
            table.addCell("Emp ID");
            table.addCell("Employee Name");
            table.addCell("Basic Salary");
            table.addCell("Allowances");
            table.addCell("Deductions");
            table.addCell("Net Salary");
            
            for (Salary salary : salaries) {
                Employee employee = employeeDAO.getEmployeeById(salary.getEmployeeId());
                if (employee != null) {
                    table.addCell(String.valueOf(employee.getEmployeeId()));
                    table.addCell(employee.getFirstName() + " " + employee.getLastName());
                    table.addCell(String.format("$%.2f", salary.getBasicSalary()));
                    table.addCell(String.format("$%.2f", salary.getAllowances()));
                    table.addCell(String.format("$%.2f", salary.getDeductions()));
                    table.addCell(String.format("$%.2f", salary.getNetSalary()));
                    
                    totalBasic += salary.getBasicSalary();
                    totalAllowances += salary.getAllowances();
                    totalDeductions += salary.getDeductions();
                    totalNet += salary.getNetSalary();
                }
            }
            
            document.add(table);
            document.add(new Paragraph(" "));
            
            // Summary
            document.add(new Paragraph("Summary:"));
            document.add(new Paragraph("Total Basic Salary: $" + String.format("%.2f", totalBasic)));
            document.add(new Paragraph("Total Allowances: $" + String.format("%.2f", totalAllowances)));
            document.add(new Paragraph("Total Deductions: $" + String.format("%.2f", totalDeductions)));
            document.add(new Paragraph("Total Net Salary: $" + String.format("%.2f", totalNet)));
            
            document.close();
            
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error generating payroll report", e);
        }
    }
}
