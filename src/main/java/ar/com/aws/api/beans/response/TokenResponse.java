package ar.com.aws.api.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TokenResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TokenResponse {

	private String token;
	
	public TokenResponse() {}
	
	public TokenResponse(String t) {
		this.setToken(t);
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	

}
