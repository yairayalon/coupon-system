package com.yairayalon.couponsservercommon.exceptions;

import com.yairayalon.couponsservercommon.enums.ErrorType;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private ErrorType errorType;

	// We use this constructor, each time we are the side that throws the exception
	public ApplicationException(ErrorType errorType, String message) {
		super(message);
		this.errorType = errorType;
	}

	// This version of constructor is being used, when we wrap a 3rd party
	// exception.
	public ApplicationException(Exception e, ErrorType errorType, String message) {
		super(message, e);
		this.errorType = errorType;
	}

	public ErrorType getErrorType() {
		return errorType;
	}

}