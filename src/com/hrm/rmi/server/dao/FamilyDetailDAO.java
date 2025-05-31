// File: com/hrm/rmi/server/dao/FamilyDetailDAO.java
package com.hrm.rmi.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.hrm.rmi.common.model.FamilyDetail;

public class FamilyDetailDAO {
    
    public boolean addFamilyDetail(FamilyDetail familyDetail) {
        String sql = "INSERT INTO family_details (employee_id, relation_type, first_name, last_name, contact_number) " +
                     "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, familyDetail.getEmployeeId());
            pstmt.setString(2, familyDetail.getRelationType());
            pstmt.setString(3, familyDetail.getFirstName());
            pstmt.setString(4, familyDetail.getLastName());
            pstmt.setString(5, familyDetail.getContactNumber());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    familyDetail.setFamilyId(generatedKeys.getInt(1));
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
    
    public FamilyDetail getFamilyDetailById(int familyId) {
        String sql = "SELECT * FROM family_details WHERE family_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, familyId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractFamilyDetailFromResultSet(rs);
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
    
    public List<FamilyDetail> getFamilyDetailsByEmployeeId(int employeeId) {
        List<FamilyDetail> familyDetails = new ArrayList<>();
        String sql = "SELECT * FROM family_details WHERE employee_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, employeeId);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                familyDetails.add(extractFamilyDetailFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
        
        return familyDetails;
    }
    
    public boolean updateFamilyDetail(FamilyDetail familyDetail) {
        String sql = "UPDATE family_details SET relation_type = ?, first_name = ?, last_name = ?, " +
                     "contact_number = ? WHERE family_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, familyDetail.getRelationType());
            pstmt.setString(2, familyDetail.getFirstName());
            pstmt.setString(3, familyDetail.getLastName());
            pstmt.setString(4, familyDetail.getContactNumber());
            pstmt.setInt(5, familyDetail.getFamilyId());
            
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
    
    public boolean deleteFamilyDetail(int familyId) {
        String sql = "DELETE FROM family_details WHERE family_id = ?";
        Connection conn = null;
        
        try {
            conn = DatabaseConnector.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, familyId);
            
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
    
    private FamilyDetail extractFamilyDetailFromResultSet(ResultSet rs) throws SQLException {
        FamilyDetail familyDetail = new FamilyDetail();
        
        familyDetail.setFamilyId(rs.getInt("family_id"));
        familyDetail.setEmployeeId(rs.getInt("employee_id"));
        familyDetail.setRelationType(rs.getString("relation_type"));
        familyDetail.setFirstName(rs.getString("first_name"));
        familyDetail.setLastName(rs.getString("last_name"));
        familyDetail.setContactNumber(rs.getString("contact_number"));
        familyDetail.setCreatedAt(rs.getTimestamp("created_at"));
        familyDetail.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return familyDetail;
    }
}