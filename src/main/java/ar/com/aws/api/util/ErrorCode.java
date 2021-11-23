package ar.com.aws.api.util;

public enum ErrorCode {
	INFO_INCOMPLETE(1, "Información incompleta"),
	PASSW_INCORRECTO(2, "Password incorrecto"),
	BO_ERROR(3, "Bussines Exception"),
	USER_DISABLED(4, "Usuario deshabilitado");
	
	private int code;
	
	private String description;
	
	private ErrorCode(int code, String desc) {
		this.setCode(code);
		this.setDescription(desc);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
