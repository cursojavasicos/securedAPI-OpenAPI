package ar.com.aws.api.bo;

import java.util.List;

import ar.com.aws.api.beans.UserInfo;
import ar.com.aws.api.exceptions.BOException;

public interface UserBO {

	UserInfo getUserByName(String name) throws BOException;
	
	void addUser(UserInfo user) throws BOException;
	
	void updateUser(UserInfo user) throws BOException;

	void setStatus(String userName, boolean status) throws BOException;

	void deleteUser(String userName) throws BOException;
	
	List<UserInfo> getAllUsers() throws BOException;
	
	void assignRol(String userName, String rolName) throws BOException;

	void unAssignRol(String userName, String rolName) throws BOException;
}
