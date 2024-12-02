package com.yairayalon.coupons.utils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

import com.yairayalon.coupons.dao.CompanyDao;
import com.yairayalon.coupons.dao.CustomerDao;
import com.yairayalon.coupons.enums.ClientType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

public class CookieUtils {

	public static NewCookie createSessionCookie(long clientId, int clientTypeId) throws ApplicationException {

		String idCookieValue = String.valueOf(clientId);
		String typeCookieValue = String.valueOf(clientTypeId);
		NewCookie sessionCookie = null;

		String hashedCookieClientType = HashUtils.sha256(typeCookieValue);

		String cookieValue = idCookieValue + "-" + hashedCookieClientType;
		String base64CookieValue = new String(Base64.getEncoder().encode(cookieValue.getBytes()));

		sessionCookie = new NewCookie("couponsSession", base64CookieValue, "/", "localhost", "", -1, false);

		return sessionCookie;

	}

	public static javax.servlet.http.Cookie getSessionCookie(javax.servlet.http.Cookie[] cookies) {

		if (cookies == null) {
			return null;
		}
		Map<String, javax.servlet.http.Cookie> cookieMap = new HashMap<>();
		for (javax.servlet.http.Cookie cookie : cookies) {
			cookieMap.put(cookie.getName(), cookie);
		}
		javax.servlet.http.Cookie sessionCookie = cookieMap.get("couponsSession");
		return sessionCookie;

	}
	
	public static Map<String, ClientType> createSessionToken(Cookie sessionCookie) throws ApplicationException {
		
		String codedCookieValue = sessionCookie.getValue();
		String decodedCookieValue = new String(Base64.getDecoder().decode(codedCookieValue.getBytes()));
		
		String[] cookieSplitArray = decodedCookieValue.split("-");
		long clientId = Long.parseLong(cookieSplitArray[0]);
		String hashedClientTypeIDfromCookie = cookieSplitArray[1];
		
		ClientType[] clientTypes = ClientType.values();
		Map<String, ClientType> clientTypeHashMap = new HashMap<String, ClientType>();

		for (int i = 0; i <= clientTypes.length; i++) {
			String clientTypeId = String.valueOf(clientTypes[i].getClientTypeId());
			String hashedClientTypeID = HashUtils.sha256(clientTypeId);
			
			clientTypeHashMap.put(hashedClientTypeID, clientTypes[i]);

			if (hashedClientTypeID.equals(hashedClientTypeIDfromCookie)) {
				ClientType currentClientType = clientTypeHashMap.get(hashedClientTypeID);
				Map<String, ClientType> token = new HashMap<String, ClientType>();
				token.put(String.valueOf(clientId), ClientType.getClientTypeByClientTypeId(i+1));
				
				if (currentClientType.equals(ClientType.ADMIN)) {
					return token;
				}
				else if (currentClientType.equals(ClientType.COMPANY)) {
					CompanyDao companyDao = new CompanyDao();
					if (companyDao.isCompanyExistById(clientId)) {
						return token;
					}
				}
				else if (currentClientType.equals(ClientType.CUSTOMER)) {
					CustomerDao customerDao = new CustomerDao();
					if (customerDao.isCustomerExistById(clientId)) {
						return token;
					}
				}
			}
		}
		return null;
		
	}

}