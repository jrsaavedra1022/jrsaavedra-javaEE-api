package com.jrsaavedra.util;

public class CustomErrorType {
	//JWT tokens add https://platzi.com/tutoriales/1464-jee/975-spring-security-y-json-web-token/
	private String errorMessage;

	//constructor
	public CustomErrorType(String errorMessage) {
		super();
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
}
