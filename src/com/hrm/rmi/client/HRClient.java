// File: com/hrm/rmi/client/HRClient.java
package com.hrm.rmi.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import com.hrm.rmi.common.model.Employee;
import com.hrm.rmi.common.model.User;
import com.hrm.rmi.common.service.HRMService;

public class HRClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            HRMService hrmService = (HRMService) registry.lookup("HRMService");
            User user = hrmService.getAuthenticationService().login("admin", "admin123");

            if (user == null || !user.isHR()) {
                System.out.println("Login failed or not an HR user.");
                return;
            }

            System.out.println("Login successful. Welcome HR: " + user.getUsername());

            List<Employee> employees = hrmService.getEmployeeService().getAllEmployees();
            System.out.println("Employee List:");
            for (Employee emp : employees) {
                System.out.println("- " + emp.getEmployeeId() + ": " + emp.getFirstName() + " " + emp.getLastName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
