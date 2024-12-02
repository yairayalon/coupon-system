package com.yairayalon.coupons.api;

import java.util.Collection;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;

import com.yairayalon.coupons.beans.Customer;
import com.yairayalon.coupons.enums.ClientType;
import com.yairayalon.coupons.logic.CustomerController;
import com.yairayalon.coupons.utils.CookieUtils;
import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class CustomerApi {

	private CustomerController customerController;

	public CustomerApi() {
		this.customerController = new CustomerController();
	}

	@POST
	public void createCustomer(Customer customer) throws ApplicationException {
		this.customerController.createCustomer(customer);
	}

	@DELETE
	@Path("/{customerId}")
	public void reomoveCustomer(@PathParam("customerId") long customerId) throws ApplicationException {
		this.customerController.removeCustomer(customerId);
	}

	@PUT
	public void updateCustomer(Customer customer) throws ApplicationException {
		this.customerController.updateCustomer(customer);
	}

	@GET
	@Path("/{customerId}")
	public Customer getCustomer(@CookieParam("couponsSession") Cookie cookie, @PathParam("customerId") long customerId) throws ApplicationException {
		Map<String, ClientType> token = CookieUtils.createSessionToken(cookie);
		if (token.containsValue(ClientType.ADMIN) || (token.containsValue(ClientType.CUSTOMER) && token.containsKey(String.valueOf(customerId)))) {
			Customer customer = this.customerController.getCustomer(customerId);
			return customer;
		}
		else {
			throw new ApplicationException(ErrorType.INVALID_COOKIE, "Invalid cookie or unauthorized use with cookie");
		}
	}

	@GET
	public Collection<Customer> getAllCustomers(@CookieParam("couponsSession") Cookie cookie) throws ApplicationException {
		Map<String, ClientType> token = CookieUtils.createSessionToken(cookie);
		if (token.containsValue(ClientType.ADMIN)) {
			Collection<Customer> allCustomers = this.customerController.getAllCustomers();
			return allCustomers;
		}
		else {
			throw new ApplicationException(ErrorType.INVALID_COOKIE, "Invalid cookie or unauthorized use with cookie");
		}
	}

}