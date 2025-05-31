package com.hrm.rmi.server.util;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import com.hrm.rmi.common.model.Employee;
import com.hrm.rmi.common.model.FamilyDetail;
import com.hrm.rmi.common.model.LeaveApplication;
import com.hrm.rmi.common.model.LeaveBalance;
import com.hrm.rmi.server.dao.EmployeeDAO;
import com.hrm.rmi.server.dao.FamilyDetailDAO;
import com.hrm.rmi.server.dao.LeaveApplicationDAO;
import com.hrm.rmi.server.dao.LeaveBalanceDAO;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ReportGenerator {
    
    private EmployeeDAO employeeDAO;
    private FamilyDetailDAO familyDetailDAO;
    private LeaveApplicationDAO leaveApplicationDAO;
    private LeaveBalanceDAO leaveBalanceDAO;
    
    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
    private static final Font SUBTITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
    private static final Font NORMAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10);
    
    public ReportGenerator() {
        this.employeeDAO = new EmployeeDAO();
        this.familyDetailDAO = new FamilyDetailDAO();
        this.leaveApplicationDAO = new LeaveApplicationDAO();
        this.leaveBalanceDAO = new LeaveBalanceDAO();
    }
    
    public byte[] generateEmployeeProfileReport(int employeeId) {
        try {
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            if (employee == null) {
                return null;
            }
            
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Add title
            Paragraph title = new Paragraph("Employee Profile Report", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            document.add(new Paragraph(" ")); // Add empty line
            
            // Add employee details
            document.add(new Paragraph("Employee ID: " + employee.getEmployeeId(), NORMAL_FONT));
            document.add(new Paragraph("Name: " + employee.getFirstName() + " " + employee.getLastName(), NORMAL_FONT));
            document.add(new Paragraph("IC/Passport: " + employee.getIcPassport(), NORMAL_FONT));
            document.add(new Paragraph("Email: " + (employee.getEmail() != null ? employee.getEmail() : "N/A"), NORMAL_FONT));
            document.add(new Paragraph("Phone: " + (employee.getPhone() != null ? employee.getPhone() : "N/A"), NORMAL_FONT));
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            document.add(new Paragraph("Date Hired: " + (employee.getDateHired() != null ? dateFormat.format(employee.getDateHired()) : "N/A"), NORMAL_FONT));
            
            document.add(new Paragraph("Department: " + (employee.getDepartment() != null ? employee.getDepartment() : "N/A"), NORMAL_FONT));
            document.add(new Paragraph("Position: " + (employee.getPosition() != null ? employee.getPosition() : "N/A"), NORMAL_FONT));
            
            document.close();
            
            return baos.toByteArray();
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public byte[] generateEmployeeFamilyDetailsReport(int employeeId) {
        try {
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            if (employee == null) {
                return null;
            }
            
            List<FamilyDetail> familyDetails = familyDetailDAO.getFamilyDetailsByEmployeeId(employeeId);
            
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Add title
            Paragraph title = new Paragraph("Employee Family Details Report", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            document.add(new Paragraph(" ")); // Add empty line
            
            // Add employee info
            document.add(new Paragraph("Employee: " + employee.getFirstName() + " " + employee.getLastName() + " (ID: " + employee.getEmployeeId() + ")", SUBTITLE_FONT));
            
            document.add(new Paragraph(" ")); // Add empty line
            
            if (familyDetails.isEmpty()) {
                document.add(new Paragraph("No family details found.", NORMAL_FONT));
            } else {
                // Create table for family details
                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                
                // Add table headers
                PdfPCell cell1 = new PdfPCell(new Phrase("Relation Type", SUBTITLE_FONT));
                PdfPCell cell2 = new PdfPCell(new Phrase("First Name", SUBTITLE_FONT));
                PdfPCell cell3 = new PdfPCell(new Phrase("Last Name", SUBTITLE_FONT));
                PdfPCell cell4 = new PdfPCell(new Phrase("Contact Number", SUBTITLE_FONT));
                
                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                
                // Add data rows
                for (FamilyDetail detail : familyDetails) {
                    table.addCell(new Phrase(detail.getRelationType(), NORMAL_FONT));
                    table.addCell(new Phrase(detail.getFirstName(), NORMAL_FONT));
                    table.addCell(new Phrase(detail.getLastName(), NORMAL_FONT));
                    table.addCell(new Phrase(detail.getContactNumber() != null ? detail.getContactNumber() : "N/A", NORMAL_FONT));
                }
                
                document.add(table);
            }
            
            document.close();
            
            return baos.toByteArray();
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public byte[] generateEmployeeLeaveHistoryReport(int employeeId, int year) {
        try {
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            if (employee == null) {
                return null;
            }
            
            List<LeaveApplication> leaveApplications = leaveApplicationDAO.getLeaveApplicationsByEmployeeId(employeeId);
            List<LeaveBalance> leaveBalances = leaveBalanceDAO.getLeaveBalancesByEmployeeId(employeeId, year);
            
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Add title
            Paragraph title = new Paragraph("Employee Leave History Report for " + year, TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            document.add(new Paragraph(" ")); // Add empty line
            
            // Add employee info
            document.add(new Paragraph("Employee: " + employee.getFirstName() + " " + employee.getLastName() + " (ID: " + employee.getEmployeeId() + ")", SUBTITLE_FONT));
            
            document.add(new Paragraph(" ")); // Add empty line
            
            // Leave balances section
            document.add(new Paragraph("Leave Balances", SUBTITLE_FONT));
            
            if (leaveBalances.isEmpty()) {
                document.add(new Paragraph("No leave balances found for the year " + year, NORMAL_FONT));
            } else {
                // Create table for leave balances
                PdfPTable balanceTable = new PdfPTable(4);
                balanceTable.setWidthPercentage(100);
                
                // Add table headers
                PdfPCell cell1 = new PdfPCell(new Phrase("Leave Type", SUBTITLE_FONT));
                PdfPCell cell2 = new PdfPCell(new Phrase("Total Days", SUBTITLE_FONT));
                PdfPCell cell3 = new PdfPCell(new Phrase("Used Days", SUBTITLE_FONT));
                PdfPCell cell4 = new PdfPCell(new Phrase("Remaining Days", SUBTITLE_FONT));
                
                balanceTable.addCell(cell1);
                balanceTable.addCell(cell2);
                balanceTable.addCell(cell3);
                balanceTable.addCell(cell4);
                
                // Add data rows
                for (LeaveBalance balance : leaveBalances) {
                    balanceTable.addCell(new Phrase(balance.getLeaveTypeName(), NORMAL_FONT));
                    balanceTable.addCell(new Phrase(String.valueOf(balance.getTotalDays()), NORMAL_FONT));
                    balanceTable.addCell(new Phrase(String.valueOf(balance.getUsedDays()), NORMAL_FONT));
                    balanceTable.addCell(new Phrase(String.valueOf(balance.getRemainingDays()), NORMAL_FONT));
                }
                
                document.add(balanceTable);
            }
            
            document.add(new Paragraph(" ")); // Add empty line
            
            // Leave applications section
            document.add(new Paragraph("Leave Applications", SUBTITLE_FONT));
            
            if (leaveApplications.isEmpty()) {
                document.add(new Paragraph("No leave applications found.", NORMAL_FONT));
            } else {
                // Create table for leave applications
                PdfPTable applicationTable = new PdfPTable(5);
                applicationTable.setWidthPercentage(100);
                
                // Add table headers
                PdfPCell cell1 = new PdfPCell(new Phrase("Leave Type", SUBTITLE_FONT));
                PdfPCell cell2 = new PdfPCell(new Phrase("Start Date", SUBTITLE_FONT));
                PdfPCell cell3 = new PdfPCell(new Phrase("End Date", SUBTITLE_FONT));
                PdfPCell cell4 = new PdfPCell(new Phrase("Total Days", SUBTITLE_FONT));
                PdfPCell cell5 = new PdfPCell(new Phrase("Status", SUBTITLE_FONT));
                
                applicationTable.addCell(cell1);
                applicationTable.addCell(cell2);
                applicationTable.addCell(cell3);
                applicationTable.addCell(cell4);
                applicationTable.addCell(cell5);
                
                // Add data rows
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                for (LeaveApplication application : leaveApplications) {
                    applicationTable.addCell(new Phrase(application.getLeaveTypeName(), NORMAL_FONT));
                    applicationTable.addCell(new Phrase(dateFormat.format(application.getStartDate()), NORMAL_FONT));
                    applicationTable.addCell(new Phrase(dateFormat.format(application.getEndDate()), NORMAL_FONT));
                    applicationTable.addCell(new Phrase(String.valueOf(application.getTotalDays()), NORMAL_FONT));
                    applicationTable.addCell(new Phrase(application.getStatus(), NORMAL_FONT));
                }
                
                document.add(applicationTable);
            }
            
            document.close();
            
            return baos.toByteArray();
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public byte[] generateEmployeeYearlyReport(int employeeId, int year) {
        try {
            // This combines all three reports into one comprehensive report
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            if (employee == null) {
                return null;
            }
            
            List<FamilyDetail> familyDetails = familyDetailDAO.getFamilyDetailsByEmployeeId(employeeId);
            List<LeaveApplication> leaveApplications = leaveApplicationDAO.getLeaveApplicationsByEmployeeId(employeeId);
            List<LeaveBalance> leaveBalances = leaveBalanceDAO.getLeaveBalancesByEmployeeId(employeeId, year);
            
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Add title
            Paragraph title = new Paragraph("Employee Yearly Report - " + year, TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            document.add(new Paragraph(" ")); // Add empty line
            
            // SECTION 1: Employee Profile
            Paragraph profileTitle = new Paragraph("1. Employee Profile", SUBTITLE_FONT);
            document.add(profileTitle);
            
            document.add(new Paragraph(" ")); // Add empty line
            
            // Add employee details
            document.add(new Paragraph("Employee ID: " + employee.getEmployeeId(), NORMAL_FONT));
            document.add(new Paragraph("Name: " + employee.getFirstName() + " " + employee.getLastName(), NORMAL_FONT));
            document.add(new Paragraph("IC/Passport: " + employee.getIcPassport(), NORMAL_FONT));
            document.add(new Paragraph("Email: " + (employee.getEmail() != null ? employee.getEmail() : "N/A"), NORMAL_FONT));
            document.add(new Paragraph("Phone: " + (employee.getPhone() != null ? employee.getPhone() : "N/A"), NORMAL_FONT));
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            document.add(new Paragraph("Date Hired: " + (employee.getDateHired() != null ? dateFormat.format(employee.getDateHired()) : "N/A"), NORMAL_FONT));
            
            document.add(new Paragraph("Department: " + (employee.getDepartment() != null ? employee.getDepartment() : "N/A"), NORMAL_FONT));
            document.add(new Paragraph("Position: " + (employee.getPosition() != null ? employee.getPosition() : "N/A"), NORMAL_FONT));
            
            document.add(new Paragraph(" ")); // Add empty line
            
            // SECTION 2: Family Details
            Paragraph familyTitle = new Paragraph("2. Family Details", SUBTITLE_FONT);
            document.add(familyTitle);
            
            document.add(new Paragraph(" ")); // Add empty line
            
            if (familyDetails.isEmpty()) {
                document.add(new Paragraph("No family details found.", NORMAL_FONT));
            } else {
                // Create table for family details
                PdfPTable familyTable = new PdfPTable(4);
                familyTable.setWidthPercentage(100);
                
                // Add table headers
                PdfPCell cell1 = new PdfPCell(new Phrase("Relation Type", SUBTITLE_FONT));
                PdfPCell cell2 = new PdfPCell(new Phrase("First Name", SUBTITLE_FONT));
                PdfPCell cell3 = new PdfPCell(new Phrase("Last Name", SUBTITLE_FONT));
                PdfPCell cell4 = new PdfPCell(new Phrase("Contact Number", SUBTITLE_FONT));
                
                familyTable.addCell(cell1);
                familyTable.addCell(cell2);
                familyTable.addCell(cell3);
                familyTable.addCell(cell4);
                
                // Add data rows
                for (FamilyDetail detail : familyDetails) {
                    familyTable.addCell(new Phrase(detail.getRelationType(), NORMAL_FONT));
                    familyTable.addCell(new Phrase(detail.getFirstName(), NORMAL_FONT));
                    familyTable.addCell(new Phrase(detail.getLastName(), NORMAL_FONT));
                    familyTable.addCell(new Phrase(detail.getContactNumber() != null ? detail.getContactNumber() : "N/A", NORMAL_FONT));
                }
                
                document.add(familyTable);
            }
            
            document.add(new Paragraph(" ")); // Add empty line
            
            // SECTION 3: Leave History
            Paragraph leaveTitle = new Paragraph("3. Leave History", SUBTITLE_FONT);
            document.add(leaveTitle);
            
            document.add(new Paragraph(" ")); // Add empty line
            
            // Leave balances sub-section
            document.add(new Paragraph("3.1 Leave Balances", SUBTITLE_FONT));
            
            if (leaveBalances.isEmpty()) {
                document.add(new Paragraph("No leave balances found for the year " + year, NORMAL_FONT));
            } else {
                // Create table for leave balances
                PdfPTable balanceTable = new PdfPTable(4);
                balanceTable.setWidthPercentage(100);
                
                // Add table headers
                PdfPCell cell1 = new PdfPCell(new Phrase("Leave Type", SUBTITLE_FONT));
                PdfPCell cell2 = new PdfPCell(new Phrase("Total Days", SUBTITLE_FONT));
                PdfPCell cell3 = new PdfPCell(new Phrase("Used Days", SUBTITLE_FONT));
                PdfPCell cell4 = new PdfPCell(new Phrase("Remaining Days", SUBTITLE_FONT));
                
                balanceTable.addCell(cell1);
                balanceTable.addCell(cell2);
                balanceTable.addCell(cell3);
                balanceTable.addCell(cell4);
                
                // Add data rows
                for (LeaveBalance balance : leaveBalances) {
                    balanceTable.addCell(new Phrase(balance.getLeaveTypeName(), NORMAL_FONT));
                    balanceTable.addCell(new Phrase(String.valueOf(balance.getTotalDays()), NORMAL_FONT));
                    balanceTable.addCell(new Phrase(String.valueOf(balance.getUsedDays()), NORMAL_FONT));
                    balanceTable.addCell(new Phrase(String.valueOf(balance.getRemainingDays()), NORMAL_FONT));
                }
                
                document.add(balanceTable);
            }
            
            document.add(new Paragraph(" ")); // Add empty line
            
            // Leave applications sub-section
            document.add(new Paragraph("3.2 Leave Applications", SUBTITLE_FONT));
            
            if (leaveApplications.isEmpty()) {
                document.add(new Paragraph("No leave applications found.", NORMAL_FONT));
            } else {
                // Create table for leave applications
                PdfPTable applicationTable = new PdfPTable(5);
                applicationTable.setWidthPercentage(100);
                
                // Add table headers
                PdfPCell cell1 = new PdfPCell(new Phrase("Leave Type", SUBTITLE_FONT));
                PdfPCell cell2 = new PdfPCell(new Phrase("Start Date", SUBTITLE_FONT));
                PdfPCell cell3 = new PdfPCell(new Phrase("End Date", SUBTITLE_FONT));
                PdfPCell cell4 = new PdfPCell(new Phrase("Total Days", SUBTITLE_FONT));
                PdfPCell cell5 = new PdfPCell(new Phrase("Status", SUBTITLE_FONT));
                
                applicationTable.addCell(cell1);
                applicationTable.addCell(cell2);
                applicationTable.addCell(cell3);
                applicationTable.addCell(cell4);
                applicationTable.addCell(cell5);
                
                // Add data rows
                for (LeaveApplication application : leaveApplications) {
                    applicationTable.addCell(new Phrase(application.getLeaveTypeName(), NORMAL_FONT));
                    applicationTable.addCell(new Phrase(dateFormat.format(application.getStartDate()), NORMAL_FONT));
                    applicationTable.addCell(new Phrase(dateFormat.format(application.getEndDate()), NORMAL_FONT));
                    applicationTable.addCell(new Phrase(String.valueOf(application.getTotalDays()), NORMAL_FONT));
                    applicationTable.addCell(new Phrase(application.getStatus(), NORMAL_FONT));
                }
                
                document.add(applicationTable);
            }
            
            document.close();
            
            return baos.toByteArray();
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public byte[] generateAllEmployeesReport() {
        try {
            List<Employee> employees = employeeDAO.getAllEmployees();
            
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            
            document.open();
            
            // Add title
            Paragraph title = new Paragraph("All Employees Report", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            
            document.add(new Paragraph(" ")); // Add empty line
            
            if (employees.isEmpty()) {
                document.add(new Paragraph("No employees found.", NORMAL_FONT));
            } else {
                // Create table for employees
                PdfPTable table = new PdfPTable(6);
                table.setWidthPercentage(100);
                
                // Add table headers
                PdfPCell cell1 = new PdfPCell(new Phrase("ID", SUBTITLE_FONT));
                PdfPCell cell2 = new PdfPCell(new Phrase("First Name", SUBTITLE_FONT));
                PdfPCell cell3 = new PdfPCell(new Phrase("Last Name", SUBTITLE_FONT));
                PdfPCell cell4 = new PdfPCell(new Phrase("IC/Passport", SUBTITLE_FONT));
                PdfPCell cell5 = new PdfPCell(new Phrase("Department", SUBTITLE_FONT));
                PdfPCell cell6 = new PdfPCell(new Phrase("Position", SUBTITLE_FONT));
                
                table.addCell(cell1);
                table.addCell(cell2);
                table.addCell(cell3);
                table.addCell(cell4);
                table.addCell(cell5);
                table.addCell(cell6);
                
                // Add data rows
                for (Employee employee : employees) {
                    table.addCell(new Phrase(String.valueOf(employee.getEmployeeId()), NORMAL_FONT));
                    table.addCell(new Phrase(employee.getFirstName(), NORMAL_FONT));
                    table.addCell(new Phrase(employee.getLastName(), NORMAL_FONT));
                    table.addCell(new Phrase(employee.getIcPassport(), NORMAL_FONT));
                    table.addCell(new Phrase(employee.getDepartment() != null ? employee.getDepartment() : "N/A", NORMAL_FONT));
                    table.addCell(new Phrase(employee.getPosition() != null ? employee.getPosition() : "N/A", NORMAL_FONT));
                }
                
                document.add(table);
            }
            
            document.close();
            
            return baos.toByteArray();
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}