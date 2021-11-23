package ar.com.aws.api.dao;

import java.util.List;

import ar.com.aws.api.beans.UserInfo;
import ar.com.aws.api.exceptions.DAOException;

public interface UserDAO {

	UserInfo getUserByName(String name) throws DAOException;
	
	void addUser(UserInfo user) throws DAOException;

	void updateUser(UserInfo user) throws DAOException;

	void updateStatus(String userName, String status) throws DAOException;

	void deleteUser(String userName) throws DAOException;
	
	List<UserInfo> getAllUsers() throws DAOException;
	
	void assignRol(String userName, String rolName) throws DAOException;

	void unAssignRol(String userName, String rolName) throws DAOException;
	
	boolean foundUser(String userName);

	boolean foundAssignmentUserRol(String userName, String rol);
	
}
