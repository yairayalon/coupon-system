package com.yairayalon.coupons.logic;

import java.util.Collection;

import com.yairayalon.coupons.beans.Company;
import com.yairayalon.coupons.dao.CompanyDao;
import com.yairayalon.coupons.utils.EmailUtils;
import com.yairayalon.coupons.utils.PasswordUtils;
import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

// Company Controller provides the business logic for companies
public class CompanyController {

	private CompanyDao companyDao;

	public CompanyController() {
		this.companyDao = new CompanyDao();
	}

	public void createCompany(Company company) throws ApplicationException {
		validateCreateCompany(company.getCompanyName(), company.getPassword(), company.getEmail());
		this.companyDao.createCompany(company);
	}

	// validation of the createCompany(Company company) method
	private void validateCreateCompany(String companyName, String password, String email) throws ApplicationException {
		if (this.companyDao.isCompanyExistByName(companyName)) {
			throw new ApplicationException(ErrorType.COMPANY_NAME_ALREADY_EXISTS, "The company name '" + companyName + "' already exists.");
		}
		if (!PasswordUtils.isPasswordValid(password)) {
			throw new ApplicationException(ErrorType.INVALID_PASSWORD, "The password format is invalid.");
		}
		if (!EmailUtils.isEmailAddressValid(email)) {
			throw new ApplicationException(ErrorType.INVALID_EMAIL_ADDRESS, "The email address '" + email + "' format is invalid.");
		}
	}

	// the company is being removed, its coupons are being removed in general, and those coupons 
	// are also being deleted from the account of customers who've already purchased them earlier
	// (the DELETE CASCADE on MySQL keeps this synchronization of removal)
	public void removeCompany(long companyId) throws ApplicationException {
		validateRemoveCompany(companyId);
		this.companyDao.removeCompany(companyId);
	}

	// validation of the removeCompany(long companyId) method
	private void validateRemoveCompany(long companyId) throws ApplicationException {
		if (!this.companyDao.isCompanyExistById(companyId)) {
			throw new ApplicationException(ErrorType.COMPANY_NOT_FOUND, "Company with ID " + companyId + " not found");
		}
	}

	public void updateCompany(Company company) throws ApplicationException {
		validateUpdateCompany(company.getCompanyId(), company.getPassword(), company.getEmail());
		this.companyDao.updateCompany(company);
	}

	// validation of the updateCompany(Company company) method
	private void validateUpdateCompany(long companyId, String password, String email) throws ApplicationException {
		if (!this.companyDao.isCompanyExistById(companyId)) {
			throw new ApplicationException(ErrorType.COMPANY_NOT_FOUND, "Company with ID " + companyId + " not found");
		}
		if (!PasswordUtils.isPasswordValid(password)) {
			throw new ApplicationException(ErrorType.INVALID_PASSWORD, "The password format is invalid.");
		}
		if (!EmailUtils.isEmailAddressValid(email)) {
			throw new ApplicationException(ErrorType.INVALID_EMAIL_ADDRESS, "The email address '" + email + "' format is invalid.");
		}
	}

	public Company getCompany(long companyId) throws ApplicationException {
		Company company = this.companyDao.getCompany(companyId);
		return company;
	}

	public Collection<Company> getAllCompanies() throws ApplicationException {
		Collection<Company> allCompanies = this.companyDao.getAllCompanies();
		return allCompanies;
	}

	public boolean login(String companyName, String password) throws ApplicationException {
		boolean isLoginVerified = this.companyDao.login(companyName, password);
		return isLoginVerified;
	}
	
	public long getCompanyIdByCompanyNameAndPassword(String companyName, String password) throws ApplicationException {
		long companyId = this.companyDao.getCompanyIdByCompanyNameAndPassword(companyName, password);
		return companyId;
	}

}