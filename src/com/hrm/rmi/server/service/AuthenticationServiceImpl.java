// File: com/hrm/rmi/server/service/AuthenticationServiceImpl.java
package com.hrm.rmi.server.service;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.hrm.rmi.common.model.User;
import com.hrm.rmi.common.service.AuthenticationService;
import com.hrm.rmi.server.dao.UserDAO;

public class AuthenticationServiceImpl extends UnicastRemoteObject implements AuthenticationService {
    private static final long serialVersionUID = 1L;
    
    private UserDAO userDAO;
    
    public AuthenticationServiceImpl() throws RemoteException {
        super();
        this.userDAO = new UserDAO();
    }
    
    @Override
    public User login(String username, String password) throws RemoteException {
        try {
            System.out.println("Login attempt for: " + username);
            
            // Use the direct User lookup with username and password
            User user = userDAO.getUserByUsernameAndPassword(username, password);
            if (user != null) {
                // Don't return the password
                user.setPassword(null);
                return user;
            }
            
            System.out.println("Login failed for: " + username);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error during login: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean createUser(User user) throws RemoteException {
        try {
            // Check if username already exists
            if (userDAO.getUserByUsername(user.getUsername()) != null) {
                return false;
            }
            
            return userDAO.createUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error creating user", e);
        }
    }
    
    @Override
    public boolean changePassword(int userId, String oldPassword, String newPassword) throws RemoteException {
        try {
            // For password changes, we'll use a simplified approach
            User user = userDAO.getUserByUsername(String.valueOf(userId));
            if (user != null) {
                return userDAO.changePassword(userId, newPassword);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RemoteException("Error changing password", e);
        }
    }
}