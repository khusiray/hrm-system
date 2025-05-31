// File: com/hrm/rmi/common/service/AuthenticationService.java
package com.hrm.rmi.common.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.hrm.rmi.common.model.User;

public interface AuthenticationService extends Remote {
    User login(String username, String password) throws RemoteException;
    boolean createUser(User user) throws RemoteException;
    boolean changePassword(int userId, String oldPassword, String newPassword) throws RemoteException;
}