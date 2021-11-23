package ar.com.aws.api.beans.response;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ar.com.aws.api.beans.UserInfo;

@XmlRootElement(name = "UserListResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListUsersResponse {

	private int statusCode;
	
	@XmlElementWrapper(name="usuarios")
	@XmlElement(name="user")
	private List<UserInfo> list;
	
	public ListUsersResponse() {
		this(new ArrayList<UserInfo>());
	}

	public ListUsersResponse(List<UserInfo> l) {
		this(Status.OK.getStatusCode(), l);
	}	
	
	public ListUsersResponse(int code, List<UserInfo> l) {
		this.setStatusCode(code);
		this.setList(l);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public List<UserInfo> getList() {
		return list;
	}

	public void setList(List<UserInfo> list) {
		this.list = list;
	}
	
}
