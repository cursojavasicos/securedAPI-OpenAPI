package ar.com.aws.api.services.impl;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ar.com.aws.api.beans.UserInfo;
import ar.com.aws.api.beans.response.ErrorResponse;
import ar.com.aws.api.beans.response.ListUsersResponse;
import ar.com.aws.api.beans.response.MessageResponse;
import ar.com.aws.api.bo.UserBO;
import ar.com.aws.api.exceptions.BOException;
import ar.com.aws.api.security.ApiKeyParameter;
import ar.com.aws.api.security.Secured;
import ar.com.aws.api.security.TokenParameter;
import ar.com.aws.api.services.UserService;
import ar.com.aws.api.util.Constants;
import ar.com.aws.api.util.ErrorCode;

@Controller("user.service")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserBO userBO;

	@Secured(validateMethod = {Constants.TOKEN, Constants.APIKEY}, roles = {"ADMIN"}, unique = true)
	@Override
	public Response add(@TokenParameter String token, @ApiKeyParameter String apiKey, UserInfo data) {
		if (data == null ||
			data.getUser() == null || data.getUser().trim().isEmpty() ||
			data.getPassword() == null || data.getPassword().trim().isEmpty()) {
			
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				this.userBO.addUser(data);
				
				return Response.status(Status.OK).entity(new MessageResponse("User add success!!!")).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}

	@Override
	public Response update(UserInfo data) {
		if (data == null ||
			data.getUser() == null || data.getUser().trim().isEmpty() ||
			data.getPassword() == null || data.getPassword().trim().isEmpty()) {
			
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				this.userBO.updateUser(data);
				
				return Response.status(Status.OK).entity(new MessageResponse("User update success!!!")).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}

	@Override
	public Response enable(String userName) {
		if (userName == null || userName.trim().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				this.userBO.setStatus(userName, true);
				
				return Response.status(Status.OK).entity(new MessageResponse("User enabled success!!!")).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}

	@Override
	public Response disable(String userName) {
		if (userName == null || userName.trim().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				this.userBO.setStatus(userName, false);
				
				return Response.status(Status.OK).entity(new MessageResponse("User disabled success!!!")).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}

	@Override
	public Response delete(String userName) {
		if (userName == null || userName.trim().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				this.userBO.deleteUser(userName);
				
				return Response.status(Status.OK).entity(new MessageResponse("User deleted success!!!")).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}

	@Override
	public Response getUserbyName(String userName) {
		if (userName == null || userName.trim().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				UserInfo user = this.userBO.getUserByName(userName);
				
				return Response.status(Status.OK).entity(user).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}

	@Override
	public Response getAllUsers() {
		try {
			List<UserInfo> lUsers = this.userBO.getAllUsers();
			
			return Response.status(Status.OK).entity(new ListUsersResponse(lUsers)).build();
			
		} catch (BOException e) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
		}
	}

	@Override
	public Response assignRol(String userName, String rolName) {
		if (userName == null || userName.trim().isEmpty() ||
			rolName == null || rolName.trim().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				this.userBO.assignRol(userName, rolName);
				
				return Response.status(Status.OK).entity(new MessageResponse("Rol assigned success!!!")).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}

	@Override
	public Response unAssignRol(String userName, String rolName) {
		if (userName == null || userName.trim().isEmpty() ||
				rolName == null || rolName.trim().isEmpty()) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
			} else {
				try {
					this.userBO.unAssignRol(userName, rolName);
					
					return Response.status(Status.OK).entity(new MessageResponse("Rol unassigned success!!!")).build();
					
				} catch (BOException e) {
					return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
				}
			}
	}
	

}

