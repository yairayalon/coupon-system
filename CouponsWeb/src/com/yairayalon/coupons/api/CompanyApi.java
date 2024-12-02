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

import com.yairayalon.coupons.beans.Company;
import com.yairayalon.coupons.enums.ClientType;
import com.yairayalon.coupons.logic.CompanyController;
import com.yairayalon.coupons.utils.CookieUtils;
import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

@Path("/companies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class CompanyApi {

	private CompanyController companyController;

	public CompanyApi() {
		this.companyController = new CompanyController();
	}

	@POST
	public void createCompany(Company company) throws ApplicationException {
		this.companyController.createCompany(company);
	}

	@DELETE
	@Path("/{companyId}")
	public void reomoveCompany(@PathParam("companyId") long companyId) throws ApplicationException {
		this.companyController.removeCompany(companyId);
	}

	@PUT
	public void updateCompany(Company company) throws ApplicationException {
		this.companyController.updateCompany(company);
	}

	@GET
	@Path("/{companyId}")
	public Company getCompany(@CookieParam("couponsSession") Cookie cookie, @PathParam("companyId") long companyId) throws ApplicationException {
		Map<String, ClientType> token = CookieUtils.createSessionToken(cookie);
		if (token.containsValue(ClientType.ADMIN) || (token.containsValue(ClientType.COMPANY) && token.containsKey(String.valueOf(companyId)))) {
			Company company = this.companyController.getCompany(companyId);
			return company;
		}
		else {
			throw new ApplicationException(ErrorType.INVALID_COOKIE, "Invalid cookie or unauthorized use with cookie");
		}
	}

	@GET
	public Collection<Company> getAllCompanies(@CookieParam("couponsSession") Cookie cookie) throws ApplicationException {
		Map<String, ClientType> token = CookieUtils.createSessionToken(cookie);
		if (token.containsValue(ClientType.ADMIN)) {
			Collection<Company> allCompanies = this.companyController.getAllCompanies();
			return allCompanies;
		}
		else {
			throw new ApplicationException(ErrorType.INVALID_COOKIE, "Invalid cookie or unauthorized use with cookie");
		}
	}

}