package com.yairayalon.coupons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtils {

	// this method uses for validation of the CreateCompany(Company company) method
	// and checks if the email address entered is valid
	public static boolean isEmailAddressValid(String email) {
		String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(emailPattern);
		Matcher matcher = pattern.matcher(email);
		
		return matcher.matches();
	}

}