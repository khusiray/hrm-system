// File: com/hrm/rmi/server/dao/LeaveBalanceDAO.java
package com.hrm.rmi.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hrm.rmi.common.model.LeaveBalance;
import com.hrm.rmi.common.model.LeaveType;

public class LeaveBalanceDAO {
    
    private LeaveTypeDAO leaveTypeDAO = new LeaveTypeDAO();
    
    public LeaveBalance getLeaveBalance(int employeeId, int leaveTypeId, int year) {
        String sql = "SELECT lb.*, lt.leave_type_name FROM leave_balances lb " +
                    "JOIN leave_types lt ON lb.leave_type_id = lt.leave_type_id " +
                    "WHERE lb.employee_id = ? AND lb.leave_type_id = ? AND lb.year = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setInt(2, leaveTypeId);
            pstmt.setInt(3, year);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractLeaveBalanceFromResultSet(rs);
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
    
    public List<LeaveBalance> getLeaveBalancesByEmployeeId(int employeeId, int year) {
        List<LeaveBalance> leaveBalances = new ArrayList<>();
        String sql = "SELECT lb.*, lt.leave_type_name FROM leave_balances lb " +
                    "JOIN leave_types lt ON lb.leave_type_id = lt.leave_type_id " +
                    "WHERE lb.employee_id = ? AND lb.year = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            pstmt.setInt(2, year);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                leaveBalances.add(extractLeaveBalanceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
        
        return leaveBalances;
    }
    
    public boolean initializeLeaveBalances(int employeeId, int year) {
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            
            // Get all leave types
            List<LeaveType> leaveTypes = leaveTypeDAO.getAllLeaveTypes();
            
            // Start transaction
            conn.setAutoCommit(false);
            
            String sql = "INSERT INTO leave_balances (employee_id, leave_type_id, year, total_days, used_days) " +
                         "VALUES (?, ?, ?, ?, 0)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            for (LeaveType leaveType : leaveTypes) {
                pstmt.setInt(1, employeeId);
                pstmt.setInt(2, leaveType.getLeaveTypeId());
                pstmt.setInt(3, year);
                pstmt.setInt(4, leaveType.getDefaultDays());
                
                pstmt.addBatch();
            }
            
            int[] results = pstmt.executeBatch();
            
            // Commit the transaction
            conn.commit();
            
            // Check if all inserts were successful
            for (int result : results) {
                if (result <= 0) {
                    return false;
                }
            }
            
            return true;
        } catch (SQLException e) {
            // Rollback transaction if an error occurs
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                DatabaseConnector.releaseConnection(conn);
            }
        }
    }
    
    public boolean updateLeaveBalance(LeaveBalance leaveBalance) {
        String sql = "UPDATE leave_balances SET total_days = ?, used_days = ? " +
                     "WHERE balance_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, leaveBalance.getTotalDays());
            pstmt.setInt(2, leaveBalance.getUsedDays());
            pstmt.setInt(3, leaveBalance.getBalanceId());
            
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
    
    // Helper method to update leave balance when leave is approved
    public boolean updateLeaveBalanceOnLeaveApproval(int employeeId, int leaveTypeId, int year, int days) {
        LeaveBalance balance = getLeaveBalance(employeeId, leaveTypeId, year);
        if (balance == null) {
            return false;
        }
        
        balance.setUsedDays(balance.getUsedDays() + days);
        return updateLeaveBalance(balance);
    }
    
    private LeaveBalance extractLeaveBalanceFromResultSet(ResultSet rs) throws SQLException {
        LeaveBalance leaveBalance = new LeaveBalance();
        
        leaveBalance.setBalanceId(rs.getInt("balance_id"));
        leaveBalance.setEmployeeId(rs.getInt("employee_id"));
        leaveBalance.setLeaveTypeId(rs.getInt("leave_type_id"));
        leaveBalance.setLeaveTypeName(rs.getString("leave_type_name"));
        leaveBalance.setYear(rs.getInt("year"));
        leaveBalance.setTotalDays(rs.getInt("total_days"));
        leaveBalance.setUsedDays(rs.getInt("used_days"));
        leaveBalance.setCreatedAt(rs.getTimestamp("created_at"));
        leaveBalance.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return leaveBalance;
    }
}