package ar.com.aws.api.services.impl;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import ar.com.aws.api.beans.RolInfo;
import ar.com.aws.api.beans.response.ErrorResponse;
import ar.com.aws.api.beans.response.ListRolResponse;
import ar.com.aws.api.beans.response.MessageResponse;
import ar.com.aws.api.bo.RolBO;
import ar.com.aws.api.exceptions.BOException;
import ar.com.aws.api.services.RolService;
import ar.com.aws.api.util.ErrorCode;

@Controller("rol.service")
public class RolServiceImpl implements RolService {
	
	@Autowired
	private RolBO rolBO;

	@Override
	public Response add(RolInfo data) {
		if (data == null ||
			data.getName() == null || data.getName().trim().isEmpty()) {
			
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				this.rolBO.addRol(data);
				
				return Response.status(Status.OK).entity(new MessageResponse("Rol add success!!!")).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}

	@Override
	public Response enable(String rolName) {
		if (rolName == null || rolName.trim().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				this.rolBO.setStatus(rolName, true);
				
				return Response.status(Status.OK).entity(new MessageResponse("Rol enabled success!!!")).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}

	@Override
	public Response disable(String rolName) {
		if (rolName == null || rolName.trim().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				this.rolBO.setStatus(rolName, false);
				
				return Response.status(Status.OK).entity(new MessageResponse("Rol disabled success!!!")).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}

	@Override
	public Response delete(String rolName) {
		if (rolName == null || rolName.trim().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				this.rolBO.deleteRol(rolName);
				
				return Response.status(Status.OK).entity(new MessageResponse("Rol deleted success!!!")).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}

	@Override
	public Response getRolesOfUser(String userName) {
		if (userName == null || userName.trim().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				List<RolInfo> lRoles = this.rolBO.getRolesOfUser(userName);
				
				return Response.status(Status.OK).entity(new ListRolResponse(lRoles)).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}

	@Override
	public Response getAllRoles() {
		try {
			List<RolInfo> lRoles = this.rolBO.getAllRoles();
			
			return Response.status(Status.OK).entity(new ListRolResponse(lRoles)).build();
			
		} catch (BOException e) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
		}
	}

	@Override
	public Response getRolbyName(String rolName) {
		if (rolName == null || rolName.trim().isEmpty()) {
			return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.INFO_INCOMPLETE)).build();
		} else {
			try {
				RolInfo rol = this.rolBO.getRolByName(rolName);
				
				return Response.status(Status.OK).entity(rol).build();
				
			} catch (BOException e) {
				return Response.status(Status.BAD_REQUEST).entity(new ErrorResponse(ErrorCode.BO_ERROR, e.getMessage())).build();
			}
		}
	}
	

}

