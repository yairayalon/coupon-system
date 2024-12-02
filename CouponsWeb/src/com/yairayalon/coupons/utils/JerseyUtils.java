package com.yairayalon.coupons.utils;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

public class JerseyUtils {
	
	public static String convertObjectToJson(Object object) throws ApplicationException {
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(object);
			return json;
		} catch (IOException e) {
			throw new ApplicationException(e, ErrorType.GENERAL_ERROR, "Error in JerseyUtils, convertObjectToJson(Object object);");
		}
		
	}
	
	public static void sendJsonAsPostToWebResource(String json, String webUrl) throws ApplicationException {
		
		Client client = Client.create();
		WebResource webResource = client.resource(webUrl);
		
		ClientResponse response = webResource.type("application/json").post(ClientResponse.class, json);

		if (response.getStatus() != 200) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Error in JerseyUtils, sendJsonAsPostToWebResource(String json, String webUrl);");
		}
		
	}

}