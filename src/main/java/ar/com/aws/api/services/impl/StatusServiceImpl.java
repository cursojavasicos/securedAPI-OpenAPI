package ar.com.aws.api.services.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Controller;

import ar.com.aws.api.beans.response.MessageResponse;
import ar.com.aws.api.services.StatusService;

@Controller("status.service")
public class StatusServiceImpl implements StatusService {

	@Override
	public Response status() {
		return Response.ok().entity(new MessageResponse(Status.OK.getReasonPhrase())).build();
	}
	

}

