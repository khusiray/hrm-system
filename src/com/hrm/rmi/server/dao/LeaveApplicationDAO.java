// File: com/hrm/rmi/server/dao/LeaveApplicationDAO.java
package com.hrm.rmi.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hrm.rmi.common.model.LeaveApplication;
import com.hrm.rmi.common.model.LeaveType;

public class LeaveApplicationDAO {
    
    private LeaveTypeDAO leaveTypeDAO = new LeaveTypeDAO();
    
    public boolean applyForLeave(LeaveApplication leave) {
        String sql = "INSERT INTO leave_applications (employee_id, leave_type_id, start_date, end_date, total_days, reason, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, leave.getEmployeeId());
            pstmt.setInt(2, leave.getLeaveTypeId());
            pstmt.setDate(3, new java.sql.Date(leave.getStartDate().getTime()));
            pstmt.setDate(4, new java.sql.Date(leave.getEndDate().getTime()));
            pstmt.setInt(5, leave.getTotalDays());
            pstmt.setString(6, leave.getReason());
            pstmt.setString(7, leave.getStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    leave.setLeaveId(generatedKeys.getInt(1));
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
    
    public LeaveApplication getLeaveApplicationById(int leaveId) {
        String sql = "SELECT la.*, lt.leave_type_name FROM leave_applications la " +
                    "JOIN leave_types lt ON la.leave_type_id = lt.leave_type_id " +
                    "WHERE la.leave_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, leaveId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractLeaveApplicationFromResultSet(rs);
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
    
    public List<LeaveApplication> getLeaveApplicationsByEmployeeId(int employeeId) {
        List<LeaveApplication> leaveApplications = new ArrayList<>();
        String sql = "SELECT la.*, lt.leave_type_name FROM leave_applications la " +
                    "JOIN leave_types lt ON la.leave_type_id = lt.leave_type_id " +
                    "WHERE la.employee_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                leaveApplications.add(extractLeaveApplicationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
        
        return leaveApplications;
    }
    
    public List<LeaveApplication> getAllLeaveApplications() {
        List<LeaveApplication> leaveApplications = new ArrayList<>();
        String sql = "SELECT la.*, lt.leave_type_name FROM leave_applications la " +
                    "JOIN leave_types lt ON la.leave_type_id = lt.leave_type_id";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                leaveApplications.add(extractLeaveApplicationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
        
        return leaveApplications;
    }
    
    public boolean updateLeaveStatus(int leaveId, String status) {
        String sql = "UPDATE leave_applications SET status = ? WHERE leave_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, status);
            pstmt.setInt(2, leaveId);
            
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
    
    private LeaveApplication extractLeaveApplicationFromResultSet(ResultSet rs) throws SQLException {
        LeaveApplication leaveApplication = new LeaveApplication();
        
        leaveApplication.setLeaveId(rs.getInt("leave_id"));
        leaveApplication.setEmployeeId(rs.getInt("employee_id"));
        leaveApplication.setLeaveTypeId(rs.getInt("leave_type_id"));
        leaveApplication.setLeaveTypeName(rs.getString("leave_type_name"));
        leaveApplication.setStartDate(rs.getDate("start_date"));
        leaveApplication.setEndDate(rs.getDate("end_date"));
        leaveApplication.setTotalDays(rs.getInt("total_days"));
        leaveApplication.setReason(rs.getString("reason"));
        leaveApplication.setStatus(rs.getString("status"));
        leaveApplication.setCreatedAt(rs.getTimestamp("created_at"));
        leaveApplication.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return leaveApplication;
    }
}