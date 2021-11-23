package ar.com.aws.api.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ar.com.aws.api.beans.RolInfo;
import ar.com.aws.api.beans.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Path("rol")
@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface RolService  {

	@POST
	@Operation(description = "Agregar un rol",
			   method = HttpMethod.POST,
			   operationId = "AddRol",
			   tags = "Roles")
	@Path("/")
	Response add(RolInfo data);

	@PATCH
	@Operation(description = "Habilitar un rol",
			   method = HttpMethod.PATCH,
			   operationId = "EnableRol",
			   tags = "Roles")
	@Path("enable/{name}")
	Response enable(@PathParam("name") String rolName);
	
	@PATCH
	@Operation(description = "Deshabilitar un rol",
			   method = HttpMethod.PATCH,
			   operationId = "DisableRol",
			   tags = "Roles")
	@Path("disable/{name}")
	Response disable(@PathParam("name") String rolName);
	
	@DELETE
	@Operation(description = "Eliminar un rol",
			   method = HttpMethod.DELETE,
			   operationId = "EliminarRol",
			   tags = "Roles")
	@Path("/{name}")
	Response delete(@PathParam("name") String rolName);

	@GET
	@Operation(description = "Buscar rol por nombre",
			   method = HttpMethod.GET,
			   operationId = "GetRolByName",
			   tags = "Roles")
	@Path("/{name}")
	Response getRolbyName(@PathParam("name") String rolName);

	@GET
	@Operation(description = "Obtener los roles de un usuario",
			   method = HttpMethod.GET,
			   operationId = "GetRolesOfUser",
			   tags = "Roles",
			   responses = {@ApiResponse(responseCode = "200",
			                             description = "Lista de roles",
				   						 content = {
				   								 @Content(mediaType = MediaType.APPLICATION_JSON, 
	 		   								 			  schema = @Schema(implementation = MessageResponse.class)),
				   								 @Content(mediaType = MediaType.APPLICATION_XML, 
				   								 		  schema = @Schema(implementation = MessageResponse.class))
			   						 			})
					   				}
			   )
	@Path("/user/{userName}")
	Response getRolesOfUser(@PathParam("userName") String userName);

	@GET
	@Operation(description = "Obtener todos los roles",
			   method = HttpMethod.GET,
			   operationId = "GetAllRoles",
			   tags = "Roles")
	@Path("/")
	Response getAllRoles();
	
}

