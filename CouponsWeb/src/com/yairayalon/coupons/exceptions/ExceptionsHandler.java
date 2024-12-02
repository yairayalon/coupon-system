package com.yairayalon.coupons.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.yairayalon.coupons.beans.ErrorBean;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;
 
@Provider
public class ExceptionsHandler implements ExceptionMapper<Throwable> 
{
    @Override
    public Response toResponse(Throwable exception) {
    	if (exception instanceof ApplicationException) {
    		ApplicationException e = (ApplicationException) exception;
    		
    		int internalErrorCode = e.getErrorType().getInternalErrorCode();
    		String errorMessage = e.getMessage();
    		ErrorBean errorBean = new ErrorBean(internalErrorCode, errorMessage);
    		return Response.status(700).entity(errorBean).build();
    	}
    	
    	System.out.println(exception);
        return Response.status(500).entity("General failure").build();
    }
    
}