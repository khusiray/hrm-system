// File: com/hrm/rmi/server/dao/SalaryDAO.java
package com.hrm.rmi.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hrm.rmi.common.model.Salary;

public class SalaryDAO {
    
    public boolean addSalary(Salary salary) {
        String sql = "INSERT INTO salaries (employee_id, basic_salary, allowances, deductions, net_salary, " +
                     "payment_month, payment_year, payment_date, payment_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, salary.getEmployeeId());
            pstmt.setDouble(2, salary.getBasicSalary());
            pstmt.setDouble(3, salary.getAllowances());
            pstmt.setDouble(4, salary.getDeductions());
            pstmt.setDouble(5, salary.getNetSalary());
            pstmt.setString(6, salary.getPaymentMonth());
            pstmt.setInt(7, salary.getPaymentYear());
            
            if (salary.getPaymentDate() != null) {
                pstmt.setDate(8, new java.sql.Date(salary.getPaymentDate().getTime()));
            } else {
                pstmt.setNull(8, java.sql.Types.DATE);
            }
            
            pstmt.setString(9, salary.getPaymentStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    salary.setSalaryId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
    }
    
    public Salary getSalaryById(int salaryId) {
        String sql = "SELECT * FROM salaries WHERE salary_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, salaryId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractSalaryFromResultSet(rs);
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
    }
    
    public List<Salary> getSalariesByEmployeeId(int employeeId) {
        List<Salary> salaries = new ArrayList<>();
        String sql = "SELECT * FROM salaries WHERE employee_id = ? ORDER BY payment_year DESC, payment_month DESC";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                salaries.add(extractSalaryFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
        
        return salaries;
    }
    
    public List<Salary> getSalariesByMonth(String month, int year) {
        List<Salary> salaries = new ArrayList<>();
        String sql = "SELECT * FROM salaries WHERE payment_month = ? AND payment_year = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, month);
            pstmt.setInt(2, year);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                salaries.add(extractSalaryFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
        
        return salaries;
    }
    
    public boolean updateSalary(Salary salary) {
        String sql = "UPDATE salaries SET basic_salary = ?, allowances = ?, deductions = ?, " +
                     "net_salary = ?, payment_status = ? WHERE salary_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setDouble(1, salary.getBasicSalary());
            pstmt.setDouble(2, salary.getAllowances());
            pstmt.setDouble(3, salary.getDeductions());
            pstmt.setDouble(4, salary.calculateNetSalary());
            pstmt.setString(5, salary.getPaymentStatus());
            pstmt.setInt(6, salary.getSalaryId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
    }
    
    public boolean deleteSalary(int salaryId) {
        String sql = "DELETE FROM salaries WHERE salary_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, salaryId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
    }
    
    private Salary extractSalaryFromResultSet(ResultSet rs) throws SQLException {
        Salary salary = new Salary();
        
        salary.setSalaryId(rs.getInt("salary_id"));
        salary.setEmployeeId(rs.getInt("employee_id"));
        salary.setBasicSalary(rs.getDouble("basic_salary"));
        salary.setAllowances(rs.getDouble("allowances"));
        salary.setDeductions(rs.getDouble("deductions"));
        salary.setNetSalary(rs.getDouble("net_salary"));
        salary.setPaymentMonth(rs.getString("payment_month"));
        salary.setPaymentYear(rs.getInt("payment_year"));
        
        java.sql.Date paymentDate = rs.getDate("payment_date");
        if (paymentDate != null) {
            salary.setPaymentDate(new java.util.Date(paymentDate.getTime()));
        }
        
        salary.setPaymentStatus(rs.getString("payment_status"));
        salary.setCreatedAt(rs.getTimestamp("created_at"));
        salary.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return salary;
    }
}