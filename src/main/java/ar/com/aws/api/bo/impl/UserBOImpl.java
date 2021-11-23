package ar.com.aws.api.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.aws.api.beans.UserInfo;
import ar.com.aws.api.bo.UserBO;
import ar.com.aws.api.dao.RolDAO;
import ar.com.aws.api.dao.UserDAO;
import ar.com.aws.api.exceptions.BOException;
import ar.com.aws.api.exceptions.DAOException;

@Service
public class UserBOImpl implements UserBO {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RolDAO rolDAO;
	
	@Override
	public UserInfo getUserByName(String name) throws BOException {
		if (this.userDAO.foundUser(name)) {
			try {
				return this.userDAO.getUserByName(name);
			} catch (DAOException e) {
				throw new BOException(e);
			}
		} else {
			throw new BOException("User [" + name + "] not found!"); 
		}
	}

	@Override
	public void addUser(UserInfo user) throws BOException {
		if (this.userDAO.foundUser(user.getUser())) {
			throw new BOException("User [" + user.getUser() + "] ya existe!"); 
		} else {
			try {
				this.userDAO.addUser(user);
			} catch (DAOException e) {
				throw new BOException(e);
			}
		}
	}

	@Override
	public void updateUser(UserInfo user) throws BOException {
		if (this.userDAO.foundUser(user.getUser())) {
			try {
				this.userDAO.updateUser(user);
			} catch (DAOException e) {
				throw new BOException(e);
			}
		} else {
			throw new BOException("User [" + user.getUser() + "] not found!"); 
		}
	}

	@Override
	public void setStatus(String userName, boolean status) throws BOException {
		if (this.userDAO.foundUser(userName)) {
			try {
				this.userDAO.updateStatus(userName, (status ? "Y" : "N"));
			} catch (DAOException e) {
				throw new BOException(e);
			}
		} else {
			throw new BOException("User [" + userName + "] not found!"); 
		}
	}

	@Override
	public void deleteUser(String userName) throws BOException {
		if (this.userDAO.foundUser(userName)) {
			try {
				this.userDAO.deleteUser(userName);
			} catch (DAOException e) {
				throw new BOException(e);
			}
		} else {
			throw new BOException("User [" + userName + "] not found!"); 
		}
	}

	@Override
	public List<UserInfo> getAllUsers() throws BOException {
		try {
			return this.userDAO.getAllUsers();
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}

	@Override
	public void assignRol(String userName, String rolName) throws BOException {
		if (this.userDAO.foundUser(userName)) {
			try {
				if (this.rolDAO.foundRol(rolName)) {
					if (this.userDAO.foundAssignmentUserRol(userName, rolName)) {
						throw new BOException("User [" + userName + "] ya posee asignado el rol [" + rolName + "]");
					} else {
						this.userDAO.assignRol(userName, rolName);
					}
				} else {
					throw new BOException("Rol [" + rolName + "] not found!");
				}
				
			} catch (DAOException e) {
				throw new BOException(e);
			}
		} else {
			throw new BOException("User [" + userName + "] not found!"); 
		}
	}
	
	@Override
	public void unAssignRol(String userName, String rolName) throws BOException {
		if (this.userDAO.foundUser(userName)) {
			try {
				if (this.rolDAO.foundRol(rolName)) {
					if (this.userDAO.foundAssignmentUserRol(userName, rolName)) {
						this.userDAO.unAssignRol(userName, rolName);
					} else {
						throw new BOException("User [" + userName + "] NO posee asignado el rol [" + rolName + "]");
					}
				} else {
					throw new BOException("Rol [" + rolName + "] not found!");
				}
			} catch (DAOException e) {
				throw new BOException(e);
			}
		} else {
			throw new BOException("User [" + userName + "] not found!"); 
		}
	}
}
