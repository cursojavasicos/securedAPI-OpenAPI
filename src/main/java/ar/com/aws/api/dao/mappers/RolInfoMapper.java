package ar.com.aws.api.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import ar.com.aws.api.beans.RolInfo;

public class RolInfoMapper implements RowMapper<RolInfo> {

	@Override
	public RolInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		RolInfo r = new RolInfo();
		
		r.setRolId(rs.getInt("id"));
		r.setName(rs.getString("nombre"));
		
		r.setEnabled(rs.getString("enabled").equals("Y"));

		return r;
	}

}
