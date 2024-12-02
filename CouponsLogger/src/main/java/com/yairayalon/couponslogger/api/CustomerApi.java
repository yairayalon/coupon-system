package com.yairayalon.couponslogger.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yairayalon.couponslogger.logic.CustomerController;
import com.yairayalon.couponsservercommon.beans.CustomerIncome;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

@RestController
@RequestMapping("/customers")
public class CustomerApi {
	
	@Autowired
	private CustomerController customerController;

	@PostMapping
	public void logCustomerIncome(@RequestBody CustomerIncome customerIncome) throws ApplicationException{
		this.customerController.logCustomerIncome(customerIncome);
	}

}