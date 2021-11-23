package ar.com.aws.api.dao.impl;

import java.util.Base64;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ar.com.aws.api.beans.UserInfo;
import ar.com.aws.api.dao.UserDAO;
import ar.com.aws.api.dao.mappers.UserInfoMapper;
import ar.com.aws.api.exceptions.DAOException;

@Repository
public class UserDAOImpl implements UserDAO {

	private final Logger log = LogManager.getLogger(UserDAO.class);
	
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;
	
	@Override
	public UserInfo getUserByName(String name) throws DAOException {
		
		if (log.isDebugEnabled()) {
			log.debug("getUserByName[" + name + "]");
		}
		
		try {
			String query = "select * from user where nombre = :name";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("name", name);
			
			return jdbcTemplate.queryForObject(query, mapParameters, new UserInfoMapper());
			
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void addUser(UserInfo user) throws DAOException {
		
		if (log.isDebugEnabled()) {
			log.debug("addUser[" + user.getUser() + "]");
		}

		try {
			String query = "insert into user (nombre, password) values (:name, :passw)";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("name", user.getUser());
			mapParameters.addValue("passw", Base64.getEncoder().encode(user.getPassword().getBytes()));
			
			jdbcTemplate.update(query, mapParameters);
		
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void updateUser(UserInfo user) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("updateUser[" + user.getUser() + "]");
		}

		try {
			String query = "update user set password = :passw where nombre = :name";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("name", user.getUser());
			mapParameters.addValue("passw", Base64.getEncoder().encode(user.getPassword().getBytes()));
			
			jdbcTemplate.update(query, mapParameters);
		
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void updateStatus(String userName, String status) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("updateStatus[" + userName + ", " + status + "]");
		}

		try {
			String query = "update user set enabled = :status where nombre = :name";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("name", userName);
			mapParameters.addValue("status", status);
			
			jdbcTemplate.update(query, mapParameters);
		
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void deleteUser(String userName) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("deleteUser[" + userName + "]");
		}

		try {
			String query = "delete user where nombre = :name";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("name", userName);
			
			jdbcTemplate.update(query, mapParameters);
		
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}

	@Override
	public List<UserInfo> getAllUsers() throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("getAllUsers[]");
		}
		
		try {
			String query = "select * from user";
			
			return jdbcTemplate.query(query, new UserInfoMapper());
			
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void assignRol(String userName, String rolName) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("assignRol[" + userName + ", " + rolName + "]");
		}

		try {
			String query = "insert into user_rol_rel (user_id, rol_id) " +
						   "values ((select id " + 
						   			  "from user " + 
						   			 "where nombre = :user), " + 
						   		   "(select id " + 
						   		   	  "from rol " + 
						   		   	 "where nombre = :rol))";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("user", userName);
			mapParameters.addValue("rol", rolName);
			
			jdbcTemplate.update(query, mapParameters);
		
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void unAssignRol(String userName, String rolName) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("unAssignRol[" + userName + ", " + rolName + "]");
		}

		try {
			String query = "delete user_rol_rel urr " +
						   "where urr.user_id in (select id " + 
						   			  			   "from user " + 
						   			  			  "where nombre = :user) " + 
						   	 "and urr.rol_id in (select id " + 
						   		   	  			  "from rol " + 
						   		   	  			 "where nombre = :rol)";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("user", userName);
			mapParameters.addValue("rol", rolName);
			
			jdbcTemplate.update(query, mapParameters);
		
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}

	@Override
	public boolean foundUser(String userName) {
		if (log.isDebugEnabled()) {
			log.debug("foundUser[" + userName + "]");
		}
		
		try {
			String query = "select count(*) from user where nombre = :name";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("name", userName);
			
			Integer count = jdbcTemplate.queryForObject(query, mapParameters, Integer.class);
			
			return count > 0;
			
		} catch (Throwable e) {
			return false;
		}
	}

	@Override
	public boolean foundAssignmentUserRol(String userName, String rol) {
		if (log.isDebugEnabled()) {
			log.debug("foundAssignmentUserRol[" + userName + ", " + rol + "]");
		}
		
		try {
			String query = "select count(*) " + 
							 "from user_rol_rel urr, " + 
							 	  "user u, " + 
							 	  "rol r" + 
							"where urr.user_id = r.id " + 
							  "and urr.rol_id = r.id " + 
							  "and u.nombre = :user " + 
							  "and r.nombre = :rol ";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("user", userName);
			mapParameters.addValue("rol", rol);
			
			Integer count = jdbcTemplate.queryForObject(query, mapParameters, Integer.class);
			
			return count > 0;
			
		} catch (Throwable e) {
			return false;
		}
	}

}
