// File: com/hrm/rmi/test/EmployeeServiceTest.java
package com.hrm.rmi.test;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import com.hrm.rmi.common.model.Employee;
import com.hrm.rmi.common.service.HRMService;
import com.hrm.rmi.common.service.EmployeeService;

public class EmployeeServiceTest {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            HRMService hrmService = (HRMService) registry.lookup("HRMService");
            EmployeeService empService = hrmService.getEmployeeService();

            List<Employee> employees = empService.getAllEmployees();
            System.out.println("Total Employees: " + employees.size());

            for (Employee emp : employees) {
                System.out.println(emp.getEmployeeId() + ": " + emp.getFirstName() + " " + emp.getLastName());
            }

        } catch (Exception e) {
            System.err.println("Employee service test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
