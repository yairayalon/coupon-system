package com.yairayalon.coupons.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.yairayalon.coupons.beans.Company;
import com.yairayalon.coupons.beans.Customer;
import com.yairayalon.coupons.logic.CompanyController;
import com.yairayalon.coupons.logic.CustomerController;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

@Path("/register")
@Consumes(MediaType.APPLICATION_JSON)

public class RegisterApi {

	private CompanyController companyController;
	private CustomerController customerController;

	public RegisterApi() {
		this.companyController = new CompanyController();
		this.customerController = new CustomerController();
	}

	@POST
	@Path("/company")
	public void createCompany(Company company) throws ApplicationException {
		this.companyController.createCompany(company);
	}
	
	@POST
	@Path("/customer")
	public void createCustomer(Customer customer) throws ApplicationException {
		this.customerController.createCustomer(customer);
	}

}