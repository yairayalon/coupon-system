package com.yairayalon.couponslogger.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yairayalon.couponslogger.dao.CompanyDao;
import com.yairayalon.couponsservercommon.beans.CompanyIncome;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

@Service
public class CompanyController {

	@Autowired
	private CompanyDao companyDao;
	
	public void logCompanyIncome(CompanyIncome companyIncome) throws ApplicationException {
		this.companyDao.logCompanyIncome(companyIncome);
	}

}