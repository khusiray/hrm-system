// File: com/hrm/rmi/test/HRMServiceTest.java
package com.hrm.rmi.test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.hrm.rmi.common.service.HRMService;
import com.hrm.rmi.common.service.AuthenticationService;

public class HRMServiceTest {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            HRMService hrmService = (HRMService) registry.lookup("HRMService");

            System.out.println("RMI Ping: " + hrmService.ping());

            AuthenticationService auth = hrmService.getAuthenticationService();
            if (auth != null) {
                System.out.println("Authentication service retrieved successfully.");
            } else {
                System.out.println("Authentication service retrieval failed.");
            }
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
