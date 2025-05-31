package com.hrm.rmi.server.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/hrm_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "khusbu@12";
    
    private static final int POOL_SIZE = 10;
    private static BlockingQueue<Connection> connectionPool;
    
    static {
        try {
            initConnectionPool();
        } catch (SQLException e) {
            System.err.println("Failed to initialize connection pool: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState() + ", Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }
    
    private static void initConnectionPool() throws SQLException {
        System.out.println("Initializing connection pool with URL: " + URL + ", User: " + USER);
        connectionPool = new ArrayBlockingQueue<>(POOL_SIZE);
        for (int i = 0; i < POOL_SIZE; i++) {
            try {
                Connection conn = createConnection();
                connectionPool.offer(conn);
                System.out.println("Connection " + (i + 1) + " created successfully.");
            } catch (SQLException e) {
                System.err.println("Failed to create connection " + (i + 1) + ": " + e.getMessage());
                System.err.println("SQL State: " + e.getSQLState() + ", Error Code: " + e.getErrorCode());
                throw e;
            }
        }
    }
    
    private static Connection createConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully.");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection established successfully to: " + conn.getCatalog());
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found: " + e.getMessage(), e);
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("Attempting to retrieve connection from pool...");
            Connection connection = connectionPool.take();
            if (connection == null || connection.isClosed()) {
                System.out.println("Connection is null or closed, creating a new one...");
                connection = createConnection();
            } else if (!connection.isValid(1)) {
                System.out.println("Connection invalid, closing and recreating...");
                connection.close();
                connection = createConnection();
            }
            System.out.println("Connection retrieved successfully: " + connection.getCatalog());
            return connection;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new SQLException("Interrupted while waiting for a database connection: " + e.getMessage(), e);
        } catch (SQLException e) {
            System.err.println("Error retrieving connection: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState() + ", Error Code: " + e.getErrorCode());
            throw e;
        }
    }
    
    public static void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    System.out.println("Returning connection to pool: " + connection.getCatalog());
                    connectionPool.offer(connection);
                } else {
                    System.out.println("Connection already closed, not returning to pool.");
                }
            } catch (SQLException e) {
                System.err.println("Error checking connection state: " + e.getMessage());
            }
        }
    }
    
    public static void closeAllConnections() {
        System.out.println("Closing all connections in the pool...");
        connectionPool.forEach(conn -> {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    System.out.println("Connection closed: " + conn.getCatalog());
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}