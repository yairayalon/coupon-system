package com.yairayalon.coupons.dao;

import java.util.Collection;

import com.yairayalon.coupons.beans.Company;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

// interface to define the operation needed for a Company object
public interface ICompanyDao {

	public void createCompany(Company company) throws ApplicationException;
	public void removeCompany(long companyId) throws ApplicationException;
	public void updateCompany(Company company) throws ApplicationException;
	public Company getCompany(long companyId) throws ApplicationException;
	public Collection<Company> getAllCompanies() throws ApplicationException;
	public boolean login(String companyName, String password) throws ApplicationException;

}