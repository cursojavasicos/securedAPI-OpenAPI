package ar.com.aws.api.beans.response;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ar.com.aws.api.beans.RolInfo;

@XmlRootElement(name = "RolListResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListRolResponse {

	private int statusCode;
	
	@XmlElementWrapper(name="roles")
	@XmlElement(name="rol")
	private List<RolInfo> list;
	
	public ListRolResponse() {
		this(new ArrayList<RolInfo>());
	}

	public ListRolResponse(List<RolInfo> l) {
		this(Status.OK.getStatusCode(), l);
	}	
	
	public ListRolResponse(int code, List<RolInfo> l) {
		this.setStatusCode(code);
		this.setList(l);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public List<RolInfo> getList() {
		return list;
	}

	public void setList(List<RolInfo> list) {
		this.list = list;
	}
	
}
