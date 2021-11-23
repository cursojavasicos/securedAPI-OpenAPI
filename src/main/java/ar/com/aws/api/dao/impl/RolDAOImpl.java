package ar.com.aws.api.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ar.com.aws.api.beans.RolInfo;
import ar.com.aws.api.dao.RolDAO;
import ar.com.aws.api.dao.mappers.RolInfoMapper;
import ar.com.aws.api.exceptions.DAOException;

@Repository
public class RolDAOImpl implements RolDAO {

	private final Logger log = LogManager.getLogger(RolDAO.class);
	
	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public RolInfo getRolByName(String name) throws DAOException {
		
		if (log.isDebugEnabled()) {
			log.debug("getRolByName[" + name + "]");
		}
		
		try {
			String query = "select * from rol where nombre = :name";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("name", name);
			
			return jdbcTemplate.queryForObject(query, mapParameters, new RolInfoMapper());
			
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}
	
	@Override
	public boolean foundRol(String rolName) {
		if (log.isDebugEnabled()) {
			log.debug("foundRol[" + rolName + "]");
		}
		
		try {
			String query = "select count(*) from rol where nombre = :name";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("name", rolName);
			
			Integer count = jdbcTemplate.queryForObject(query, mapParameters, Integer.class);
			
			return count > 0;
			
		} catch (Throwable e) {
			return false;
		}
	}

	@Override
	public void addRol(RolInfo rol) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("addRol[" + rol.getName() + "]");
		}

		try {
			String query = "insert into rol (nombre) values (:name)";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("name", rol.getName());

			jdbcTemplate.update(query, mapParameters);
		
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void updateStatus(String rolName, String status) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("updateStatus[" + rolName + ", " + status + "]");
		}

		try {
			String query = "update rol set enabled = :status where nombre = :name";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("name", rolName);
			mapParameters.addValue("status", status);
			
			jdbcTemplate.update(query, mapParameters);
		
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void deleteRol(String rolName) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("deleteRol[" + rolName + "]");
		}

		try {
			String query = "delete rol where nombre = :name";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("name", rolName);
			
			jdbcTemplate.update(query, mapParameters);
		
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}

	@Override
	public List<RolInfo> getAllRoles() throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("getAllRoles[]");
		}
		
		try {
			String query = "select * from user";
			
			return jdbcTemplate.query(query, new RolInfoMapper());
			
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}

	@Override
	public List<RolInfo> getRolesOfUser(String userName) throws DAOException {
		if (log.isDebugEnabled()) {
			log.debug("getRolesOfUser[" + userName + "]");
		}
		
		try {
			String query = "select * from rol r, " + 
										 "user_rol_rel urr, " +
										 "user u " +
									 "where r.id = urr.rol_id " +
									   "and urr.user_id = u.id " +
									   "and u.nombre = :user";
			
			MapSqlParameterSource mapParameters = new MapSqlParameterSource();
			mapParameters.addValue("user", userName);
			
			return jdbcTemplate.query(query, mapParameters, new RolInfoMapper());
			
		} catch (Throwable e) {
			throw new DAOException(e);
		}
	}
	

}
