package ar.com.aws.api.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import ar.com.aws.api.beans.RolInfo;
import ar.com.aws.api.beans.UserInfo;
import ar.com.aws.api.beans.response.ErrorResponse;
import ar.com.aws.api.beans.response.TokenResponse;
import ar.com.aws.api.bo.RolBO;
import ar.com.aws.api.bo.UserBO;
import ar.com.aws.api.exceptions.BOException;
import ar.com.aws.api.services.LoginService;
import ar.com.aws.api.util.Constants;
import ar.com.aws.api.util.ErrorCode;

@Controller("login.service")
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private UserBO userBO;

	@Autowired
	private RolBO rolBO;
	
	@Override
	public Response getToken(UserInfo data) {
		
		if (data == null ||
			data.getUser() == null || data.getUser().trim().isEmpty() ||
			data.getPassword() == null || data.getPassword().trim().isEmpty()) {
			
			return Response.status(Status.UNAUTHORIZED).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		
		} else {
		
			UserInfo usr;
			try {
				usr = this.userBO.getUserByName(data.getUser());
	
				if (data.getPassword().equals(usr.getPassword())) {
					if (usr.isEnabled()) {
						
						List<RolInfo> lRoles = rolBO.getRolesOfUser(data.getUser());
						
						List<String> lStr = new ArrayList<String>();
						
						lRoles.forEach(x -> lStr.add(x.getName()));
						
						String[] aRoles = lStr.toArray(new String[lStr.size()]);
						
						Algorithm alg = Algorithm.HMAC512(Constants.SECRET);
						
						String token = JWT.create().withIssuer("AWS")
												   .withSubject("API-Authentication")
												   .withAudience("API")
												   .withJWTId(UUID.randomUUID().toString())
												   .withArrayClaim(Constants.PRIVATE_CLAIM_PREFIX + "USER_ROLES", aRoles)
												   .withExpiresAt(new Date(System.currentTimeMillis() + 50000)) //En 50 segundos se vence
												   .sign(alg);
						
						return Response.status(Status.OK).entity(new TokenResponse(token)).build();
					} else {
						return Response.status(Status.UNAUTHORIZED).entity(new ErrorResponse(ErrorCode.USER_DISABLED)).build();
					}
				} else {
					return Response.status(Status.UNAUTHORIZED).entity(new ErrorResponse(ErrorCode.PASSW_INCORRECTO)).build();
				}
	
			} catch (BOException e) {
				return Response.status(Status.UNAUTHORIZED).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		
		}
		
	}

}

