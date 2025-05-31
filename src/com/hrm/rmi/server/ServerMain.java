// File: com/hrm/rmi/server/ServerMain.java
package com.hrm.rmi.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import com.hrm.rmi.common.service.HRMService;
import com.hrm.rmi.common.util.Constants;
import com.hrm.rmi.server.dao.DatabaseConnector;
import com.hrm.rmi.server.service.HRMServiceImpl;
import com.hrm.rmi.server.util.ServerConfig;

public class ServerMain {
    
    public static void main(String[] args) {
        try {
            System.out.println("Starting HRM Server...");
            
            // Remove security manager setup as it's deprecated
            // if (System.getSecurityManager() == null) {
            //    System.setSecurityManager(new SecurityManager());
            // }
            
            // Create the service
            HRMService hrmService = new HRMServiceImpl();
            
            // Create and export the registry on the specified port
            int port = ServerConfig.getRmiPort();
            Registry registry = LocateRegistry.createRegistry(port);
            
            // Bind the service to the registry
            String serviceName = ServerConfig.getServiceName();
            registry.rebind(serviceName, hrmService);
            
            System.out.println("HRM Server started successfully on port " + port);
            System.out.println("Service bound with name: " + serviceName);
            
            // Add shutdown hook to close resources
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    try {
                        System.out.println("Shutting down HRM Server...");
                        // Unbind service
                        registry.unbind(serviceName);
                        // Unexport the service
                        UnicastRemoteObject.unexportObject(hrmService, true);
                        // Close database connections
                        DatabaseConnector.closeAllConnections();
                        System.out.println("HRM Server shutdown complete.");
                    } catch (Exception e) {
                        System.err.println("Error during shutdown: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            System.err.println("HRM Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}