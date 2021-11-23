package ar.com.aws.api.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ar.com.aws.api.beans.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Path("user")
@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface UserService  {

	@POST
	@Operation(description = "Agregar un usuario",
			   method = HttpMethod.POST,
			   operationId = "AddUser",
			   tags = "Usuarios",
			   security = {@SecurityRequirement(name = "bearerAuth"), @SecurityRequirement(name = "ApiKeyHeaderAuth")})
	@Path("/")
	Response add(@HeaderParam(HttpHeaders.AUTHORIZATION) String token,
				 @HeaderParam("X-API-KEY") String apiKey,
				 UserInfo data);

	@PUT
	@Operation(description = "Actualizar un usuario",
			   method = HttpMethod.PUT,
			   operationId = "UpdateUser",
			   tags = "Usuarios")
	@Path("/")
	Response update(UserInfo data);
	
	@PATCH
	@Operation(description = "Habilitar un usuario",
			   method = HttpMethod.PATCH,
			   operationId = "EnableUser",
			   tags = "Usuarios")
	@Path("enable/{name}")
	Response enable(@PathParam("name") String userName);
	
	@PATCH
	@Operation(description = "Deshabilitar un usuario",
			   method = HttpMethod.PATCH,
			   operationId = "DisableUser",
			   tags = "Usuarios")
	@Path("disable/{name}")
	Response disable(@PathParam("name") String userName);

	@DELETE
	@Operation(description = "Eliminar un usuario",
			   method = HttpMethod.DELETE,
			   operationId = "DeleteUser",
			   tags = "Usuarios")
	@Path("/{name}")
	Response delete(@PathParam("name") String userName);

	@GET
	@Operation(description = "Buscar un usuario por nombre",
			   method = HttpMethod.GET,
			   operationId = "GetUserByName",
			   tags = "Usuarios")
	@Path("/{name}")
	Response getUserbyName(@PathParam("name") String userName);

	@GET
	@Operation(description = "Obtener todos los usuarios",
			   method = HttpMethod.GET,
			   operationId = "GetAllUsers",
			   tags = "Usuarios")
	@Path("/")
	Response getAllUsers();

	@POST
	@Operation(description = "Asignar un rol a un usuario",
			   method = HttpMethod.POST,
			   operationId = "AsignarRol",
			   tags = "Usuarios")
	@Path("/{name}/rol/{rol}")
	Response assignRol(@PathParam("name") String userName,
					   @PathParam("rol") String rolName);

	@DELETE
	@Operation(description = "Desasignar un rol a un usuario",
			   method = HttpMethod.DELETE,
			   operationId = "DesasignarRol",
			   tags = "Usuarios")
	@Path("/{name}/rol/{rol}")
	Response unAssignRol(@PathParam("name") String userName,
						 @PathParam("rol") String rolName);
	
}

