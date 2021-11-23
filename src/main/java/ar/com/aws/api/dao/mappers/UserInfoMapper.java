package ar.com.aws.api.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import org.springframework.jdbc.core.RowMapper;

import ar.com.aws.api.beans.UserInfo;

public class UserInfoMapper implements RowMapper<UserInfo> {

	@Override
	public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserInfo u = new UserInfo();
		
		u.setUserId(rs.getInt("id"));
		u.setUser(rs.getString("nombre"));
		
		u.setPassword(new String(Base64.getDecoder().decode(rs.getString("password"))));
		
		u.setEnabled(rs.getString("enabled").equals("Y"));
		
		return u;
	}

}
