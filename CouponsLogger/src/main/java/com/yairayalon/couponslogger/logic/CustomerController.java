package com.yairayalon.couponslogger.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yairayalon.couponslogger.dao.CustomerDao;
import com.yairayalon.couponsservercommon.beans.CustomerIncome;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

@Service
public class CustomerController {
	
	@Autowired
	private CustomerDao customerDao;
	
	public void logCustomerIncome(CustomerIncome cusotmerIncome) throws ApplicationException {
		this.customerDao.logCustomerIncome(cusotmerIncome);
	}

}