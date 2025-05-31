// File: com/hrm/rmi/server/dao/LeaveTypeDAO.java
package com.hrm.rmi.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hrm.rmi.common.model.LeaveType;

public class LeaveTypeDAO {
    
    public LeaveType getLeaveTypeById(int leaveTypeId) {
        String sql = "SELECT * FROM leave_types WHERE leave_type_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, leaveTypeId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractLeaveTypeFromResultSet(rs);
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
    
    public List<LeaveType> getAllLeaveTypes() {
        List<LeaveType> leaveTypes = new ArrayList<>();
        String sql = "SELECT * FROM leave_types";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                leaveTypes.add(extractLeaveTypeFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
        
        return leaveTypes;
    }
    
    private LeaveType extractLeaveTypeFromResultSet(ResultSet rs) throws SQLException {
        LeaveType leaveType = new LeaveType();
        
        leaveType.setLeaveTypeId(rs.getInt("leave_type_id"));
        leaveType.setLeaveTypeName(rs.getString("leave_type_name"));
        leaveType.setDefaultDays(rs.getInt("default_days"));
        
        return leaveType;
    }
}