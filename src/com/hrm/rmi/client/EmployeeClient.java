// File: com/hrm/rmi/client/EmployeeClient.java
package com.hrm.rmi.client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import com.hrm.rmi.common.model.LeaveApplication;
import com.hrm.rmi.common.model.User;
import com.hrm.rmi.common.service.HRMService;

public class EmployeeClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            HRMService hrmService = (HRMService) registry.lookup("HRMService");
            User user = hrmService.getAuthenticationService().login("employee1", "password123");

            if (user == null || !user.isEmployee()) {
                System.out.println("Login failed or not an employee user.");
                return;
            }

            System.out.println("Login successful. Welcome " + user.getUsername());

            List<LeaveApplication> applications = hrmService.getEmployeeService().getLeaveApplicationsByEmployeeId(user.getEmployeeId());
            System.out.println("Leave Applications:");
            for (LeaveApplication app : applications) {
                System.out.println("- " + app.getLeaveTypeName() + ": " + app.getStartDate() + " to " + app.getEndDate() + " (" + app.getStatus() + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
