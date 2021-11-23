package ar.com.aws.api.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.aws.api.beans.RolInfo;
import ar.com.aws.api.bo.RolBO;
import ar.com.aws.api.dao.RolDAO;
import ar.com.aws.api.dao.UserDAO;
import ar.com.aws.api.exceptions.BOException;
import ar.com.aws.api.exceptions.DAOException;

@Service
public class RolBOImpl implements RolBO {

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private RolDAO rolDAO;

	@Override
	public void addRol(RolInfo rol) throws BOException {
		if (this.rolDAO.foundRol(rol.getName())) {
			throw new BOException("Rol [" + rol.getName() + "] ya existe!"); 
		} else {
			try {
				this.rolDAO.addRol(rol);
			} catch (DAOException e) {
				throw new BOException(e);
			}
		}
	}

	@Override
	public void setStatus(String rolName, boolean status) throws BOException {
		if (this.rolDAO.foundRol(rolName)) {
			try {
				this.rolDAO.updateStatus(rolName, (status ? "Y" : "N"));
			} catch (DAOException e) {
				throw new BOException(e);
			}
		} else {
			throw new BOException("Rol [" + rolName + "] not found!"); 
		}
	}

	@Override
	public void deleteRol(String rolName) throws BOException {
		if (this.rolDAO.foundRol(rolName)) {
			try {
				this.rolDAO.deleteRol(rolName);
			} catch (DAOException e) {
				throw new BOException(e);
			}
		} else {
			throw new BOException("Rol [" + rolName + "] not found!"); 
		}
	}

	@Override
	public List<RolInfo> getAllRoles() throws BOException {
		try {
			return this.rolDAO.getAllRoles();
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}

	@Override
	public List<RolInfo> getRolesOfUser(String userName) throws BOException {
		if (this.userDAO.foundUser(userName)) {
			try {
				return this.rolDAO.getRolesOfUser(userName);
			} catch (DAOException e) {
				throw new BOException(e);
			}
		} else {
			throw new BOException("User [" + userName + "] not found!"); 
		}
	}

	@Override
	public RolInfo getRolByName(String name) throws BOException {
		if (this.rolDAO.foundRol(name)) {
			try {
				return this.rolDAO.getRolByName(name);
			} catch (DAOException e) {
				throw new BOException(e);
			}
		} else {
			throw new BOException("Rol [" + name + "] not found!"); 
		}
	}
	

}
