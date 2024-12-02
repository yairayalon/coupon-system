package com.yairayalon.couponsservercommon.enums;

//enum which contains all of error types
public enum ErrorType {

	GENERAL_ERROR(600),
	
	COMPANY_NAME_ALREADY_EXISTS(605),
	COMPANY_NOT_FOUND(610),
	
	CUSTOMER_NAME_ALREADY_EXISTS(615),
	CUSTOMER_NOT_FOUND(620),
	
	COUPON_NAME_ALREADY_EXISTS(625),
	COUPON_NOT_FOUND(630),
	COUPON_OUT_OF_STOCK(635),
	COUPON_EXPIRED(640),
	
	INVALID_DATE_FORMAT(645),
	INVALID_EMAIL_ADDRESS(650),
	INVALID_PASSWORD(655),
	
	NEGATIVE_AMOUNT(660),
	NEGATIVE_PRICE(665),
	NOT_POSITIVE_QUANTITY(670),
	
	INVALID_COOKIE(675);
	
	private int internalErrorCode;

	ErrorType(int internalErrorCode){
		this.internalErrorCode = internalErrorCode;
	}

	public int getInternalErrorCode() {
		return internalErrorCode;
	}

}