package br.com.agrotis.core.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;		
	
	public AppException(String message, Throwable cause, HttpStatus status) {
		this(message, cause);
		this.status = status;		
	}
	
	public AppException(String message, Throwable cause) {
		super(message, cause);	
	}

	public AppException(String message) {
		super(message);		
	}
	
	public AppException(HttpStatus status) {
		this();
		this.status = status;		
	}
	
	public AppException() {
		super();		
	}

	public HttpStatus getStatus() {
		return status;
	}


}
