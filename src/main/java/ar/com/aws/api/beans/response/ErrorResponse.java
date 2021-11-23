package ar.com.aws.api.beans.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ar.com.aws.api.util.ErrorCode;

@XmlRootElement(name = "ErrorResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorResponse {

	private int statusCode;
	
	private String statusMessage;
	
	public ErrorResponse() {}

	public ErrorResponse(ErrorCode eCode) {
		this(eCode.getCode(), eCode.getDescription());
	}
	
	public ErrorResponse(ErrorCode eCode, String message) {
		this(eCode.getCode(), eCode.getDescription() + " - " + message);
	}

	public ErrorResponse(int code, String message) {
		this.setStatusCode(code);
		this.setStatusMessage(message);
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

}
