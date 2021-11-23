package ar.com.aws.api.bo;

import java.util.List;

import ar.com.aws.api.beans.RolInfo;
import ar.com.aws.api.exceptions.BOException;

public interface RolBO {

	RolInfo getRolByName(String name) throws BOException;

	void addRol(RolInfo rol) throws BOException;
	
	void setStatus(String rolName, boolean status) throws BOException;

	void deleteRol(String rolName) throws BOException;
	
	List<RolInfo> getAllRoles() throws BOException;
	
	List<RolInfo> getRolesOfUser(String userName) throws BOException;
}
