// File: com/hrm/rmi/server/util/ServerConfig.java
package com.hrm.rmi.server.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerConfig {
    private static Properties properties = new Properties();
    
    static {
        try {
            // Default values
            properties.setProperty("rmi.port", "1099");
            properties.setProperty("rmi.serviceName", "HRMService");
            
            // Try to load from config file if available
            try {
                properties.load(new FileInputStream("server.properties"));
            } catch (IOException e) {
                System.out.println("Config file not found, using default values.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static int getRmiPort() {
        return Integer.parseInt(properties.getProperty("rmi.port"));
    }
    
    public static String getServiceName() {
        return properties.getProperty("rmi.serviceName");
    }
}