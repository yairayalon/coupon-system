package com.yairayalon.coupons.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

public class HashUtils {

	public static String sha256(String data) throws ApplicationException {
		MessageDigest digest;
		byte[] hashedData = null;

		try {
			digest = MessageDigest.getInstance("SHA-256");
			digest.update(data.getBytes());
			hashedData = digest.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in HashUtils, sha256(String data);");
		}
		StringBuffer stringifiedHasedData = new StringBuffer();
		for (int i = 0; i < hashedData.length; i++) {
			stringifiedHasedData.append(Integer.toString((hashedData[i] & 0xff) + 0x100, 16).substring(1));
		}

		return stringifiedHasedData.toString();
	}

}