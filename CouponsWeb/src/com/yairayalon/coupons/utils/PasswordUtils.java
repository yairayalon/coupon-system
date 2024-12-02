package com.yairayalon.coupons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtils {

	// this method uses for validation of the CreateCompany(Company company) and CreateCustomer (Customer customer)
	// methods and checks if the password entered is valid - it must contain upper and lower case, digits, special character,
	//  no whitespace and be at least 8 characters long
	public static boolean isPasswordValid(String password) {
		String passwordPattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#!$%^&+=])(?=\\S+$).{8,}";
		Pattern pattern = Pattern.compile(passwordPattern);
		Matcher matcher = pattern.matcher(password);
		
		return matcher.matches();
	}
	
}