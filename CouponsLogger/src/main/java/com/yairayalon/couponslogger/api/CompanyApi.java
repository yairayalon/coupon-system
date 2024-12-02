package com.yairayalon.couponslogger.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yairayalon.couponslogger.logic.CompanyController;
import com.yairayalon.couponsservercommon.beans.CompanyIncome;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

@RestController
@RequestMapping("/companies")
public class CompanyApi {

	@Autowired
	private CompanyController companyController;

	@PostMapping
	public void logCompanyIncome(@RequestBody CompanyIncome companyIncome) throws ApplicationException{
		this.companyController.logCompanyIncome(companyIncome);
	}

}