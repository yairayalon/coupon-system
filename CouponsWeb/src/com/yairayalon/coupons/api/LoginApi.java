package com.yairayalon.coupons.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import com.yairayalon.coupons.beans.ClientIdBean;
import com.yairayalon.coupons.beans.UserLoginDetails;
import com.yairayalon.coupons.enums.ClientType;
import com.yairayalon.coupons.utils.CookieUtils;
import com.yairayalon.coupons.utils.LoginUtils;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

@Path("/login")
@Consumes(MediaType.APPLICATION_JSON)

public class LoginApi {

	@POST
	public Response login(UserLoginDetails userLoginDetails) throws ApplicationException {
		
		String name = userLoginDetails.getName();
		String password = userLoginDetails.getPassword();
		int clientTypeId = userLoginDetails.getClientTypeId();
		ClientType clientType = ClientType.getClientTypeByClientTypeId(clientTypeId);
		
		long clientId = LoginUtils.login(name, password, clientType);
		if (clientId == -1) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		else {
			NewCookie sessionCookie = CookieUtils.createSessionCookie(clientId, clientTypeId);
			ClientIdBean clientIdBean = new ClientIdBean(clientId);
			return Response.status(Response.Status.OK).cookie(sessionCookie).entity(clientIdBean).build();
		}
		
	}
	
	@DELETE
	public Response logout() throws ApplicationException {
		
		NewCookie logoutCookie = new NewCookie("couponsSession", "", "/", "localhost", null, 0, false);
		return Response.status(Response.Status.OK).cookie(logoutCookie).build();
		
	}

}