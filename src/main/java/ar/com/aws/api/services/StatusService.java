package ar.com.aws.api.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.swagger.v3.oas.annotations.Operation;

@Path("status")
@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public interface StatusService  {

	@GET
	@Operation(description = "Consultar el estado del servicio",
			   method = HttpMethod.GET,
			   operationId = "Status",
			   tags = "Status")
	@Path("/")
	Response status();

}

