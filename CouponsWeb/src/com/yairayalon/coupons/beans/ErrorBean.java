package com.yairayalon.coupons.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorBean {

	private int internalErrorCode;
	private String errorMessage;
	
	public ErrorBean() {
		super();
	}

	public ErrorBean(int internalErrorCode, String errorMessage) {
		super();
		this.internalErrorCode = internalErrorCode;
		this.errorMessage = errorMessage;
	}
	
	public int getInternalErrorCode() {
		return internalErrorCode;
	}

	public void setInternalErrorCode(int internalErrorCode) {
		this.internalErrorCode = internalErrorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}