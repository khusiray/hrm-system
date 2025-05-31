// File: com/hrm/rmi/server/dao/EmployeeDAO.java
package com.hrm.rmi.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hrm.rmi.common.model.Employee;

public class EmployeeDAO {
    
    public int addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (first_name, last_name, ic_passport, email, phone, date_hired, department, position) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getIcPassport());
            pstmt.setString(4, employee.getEmail());
            pstmt.setString(5, employee.getPhone());
            
            if (employee.getDateHired() != null) {
                pstmt.setDate(6, new java.sql.Date(employee.getDateHired().getTime()));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }
            
            pstmt.setString(7, employee.getDepartment());
            pstmt.setString(8, employee.getPosition());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    employee.setEmployeeId(id);
                    return id;
                }
            }
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
    }
    
    public Employee getEmployeeById(int id) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractEmployeeFromResultSet(rs);
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
    
    public Employee getEmployeeByIcPassport(String icPassport) {
        String sql = "SELECT * FROM employees WHERE ic_passport = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, icPassport);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractEmployeeFromResultSet(rs);
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
    
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                employees.add(extractEmployeeFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
        
        return employees;
    }
    
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, phone = ?, " +
                     "date_hired = ?, department = ?, position = ? WHERE employee_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, employee.getFirstName());
            pstmt.setString(2, employee.getLastName());
            pstmt.setString(3, employee.getEmail());
            pstmt.setString(4, employee.getPhone());
            
            if (employee.getDateHired() != null) {
                pstmt.setDate(5, new java.sql.Date(employee.getDateHired().getTime()));
            } else {
                pstmt.setNull(5, java.sql.Types.DATE);
            }
            
            pstmt.setString(6, employee.getDepartment());
            pstmt.setString(7, employee.getPosition());
            pstmt.setInt(8, employee.getEmployeeId());
            
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
    
    private Employee extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        
        employee.setEmployeeId(rs.getInt("employee_id"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setIcPassport(rs.getString("ic_passport"));
        employee.setEmail(rs.getString("email"));
        employee.setPhone(rs.getString("phone"));
        
        java.sql.Date dateHired = rs.getDate("date_hired");
        if (dateHired != null) {
            employee.setDateHired(new java.util.Date(dateHired.getTime()));
        }
        
        employee.setDepartment(rs.getString("department"));
        employee.setPosition(rs.getString("position"));
        
        employee.setCreatedAt(rs.getTimestamp("created_at"));
        employee.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return employee;
    }
}
