package com.yairayalon.coupons.utils;

import com.yairayalon.coupons.enums.ClientType;
import com.yairayalon.coupons.logic.CompanyController;
import com.yairayalon.coupons.logic.CustomerController;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

public class LoginUtils {

	// this method uses to recognize the client type trying to login (admin, company or customer),
	// and to personally customize the system to the client if he manage to login successfully.
	public static long login(String name, String password, ClientType clientType) throws ApplicationException {

		boolean isLoginVerified = false;
		long clientId;

		if (clientType.equals(ClientType.ADMIN)) {
			if (name.equals("admin") && password.equals("1234")) {
				isLoginVerified = true;
				clientId = 0;
				return clientId;
			}
		}
		else if (clientType.equals(ClientType.COMPANY)) {
			CompanyController companyController = new CompanyController();
			isLoginVerified = companyController.login(name, password);
			if (isLoginVerified) {
				clientId = companyController.getCompanyIdByCompanyNameAndPassword(name, password);
				return clientId;
			}
		}
		else if (clientType.equals(ClientType.CUSTOMER)) {
			CustomerController customerController = new CustomerController();
			isLoginVerified = customerController.login(name, password);
			if (isLoginVerified) {
				clientId = customerController.getCustomerIdByCustomerNameAndPassword(name, password);
				return clientId;
			}
		}
		return -1;

	}

}