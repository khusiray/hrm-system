// File: com/hrm/rmi/test/DatabaseTest.java
package com.hrm.rmi.test;

import java.sql.Connection;

import com.hrm.rmi.server.dao.DatabaseConnector;

public class DatabaseTest {
    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DatabaseConnector.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Database connection successful.");
            } else {
                System.out.println("Database connection failed.");
            }
        } catch (Exception e) {
            System.err.println("Database test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                DatabaseConnector.releaseConnection(conn);
            }
        }
    }
}
