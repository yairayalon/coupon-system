package com.yairayalon.coupons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
	
	public static String getCurrentDateAsString() {
		
		LocalDateTime todayDate = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String currentDate = todayDate.format(formatter);
		return currentDate;
		
	}
	
	// this method uses for validation of some of the CouponController's methods
	// and checks if the date entered is in a predefined valid format
	public static boolean isDateFormatValid(String dateToValidate, String dateFormat) {

		if(dateToValidate == null){
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);

		try {
			//if invalid, it will throw ParseException
			sdf.parse(dateToValidate);

		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	// this method uses for validation of the purchaseCoupon(long customerId, long couponId) method
	// and checks if the coupon that the customer tries to purchase has expired
	public static boolean isCouponExpired(String endDate) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String todayDate = getCurrentDateAsString();
		Date expiryDate = sdf.parse(endDate);
		Date currentDate = sdf.parse(todayDate);
		if (currentDate.after(expiryDate)) {
			return true;
		}
		return false;
	}

}