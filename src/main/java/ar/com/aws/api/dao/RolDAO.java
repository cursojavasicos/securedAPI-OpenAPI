package ar.com.aws.api.dao;

import java.util.List;

import ar.com.aws.api.beans.RolInfo;
import ar.com.aws.api.exceptions.DAOException;

public interface RolDAO {

	RolInfo getRolByName(String name) throws DAOException;
	
	void addRol(RolInfo rol) throws DAOException;

	void updateStatus(String rolName, String status) throws DAOException;

	void deleteRol(String rolName) throws DAOException;
	
	List<RolInfo> getAllRoles() throws DAOException;
	
	List<RolInfo> getRolesOfUser(String userName) throws DAOException;

	boolean foundRol(String rolName);

}
