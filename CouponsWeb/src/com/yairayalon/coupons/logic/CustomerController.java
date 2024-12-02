package com.yairayalon.coupons.logic;

import java.util.Collection;

import com.yairayalon.coupons.beans.Customer;
import com.yairayalon.coupons.dao.CustomerDao;
import com.yairayalon.coupons.utils.PasswordUtils;
import com.yairayalon.couponsservercommon.enums.ErrorType;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

// Customer Controller provides the business logic for customers
public class CustomerController {

	private CustomerDao customerDao;

	public CustomerController() {
		this.customerDao = new CustomerDao();
	}

	public void createCustomer(Customer customer) throws ApplicationException {
		validateCreateCustomer(customer.getCustomerName(), customer.getPassword());
		this.customerDao.createCustomer(customer);
	}

	// validation of the createCustomer(Customer customer) method
	private void validateCreateCustomer(String customerName, String password) throws ApplicationException {
		if (!PasswordUtils.isPasswordValid(password)) {
			throw new ApplicationException(ErrorType.INVALID_PASSWORD, "The password format is invalid.");
		}
		if (this.customerDao.isCustomerExistByName(customerName)) {
			throw new ApplicationException(ErrorType.CUSTOMER_NAME_ALREADY_EXISTS, "The customer name '" + customerName + "' already exists.");
		}
	}

	// the customer is being removed and also his coupons purchase history is being deleted
	// with himself (the DELETE CASCADE on MySQL keeps this synchronization of removal)
	public void removeCustomer(long customerId) throws ApplicationException {
		validateRemoveCustomer(customerId);
		this.customerDao.removeCustomer(customerId);
	}

	// validation of the removeCustomer(long customerId) method
	private void validateRemoveCustomer(long customerId) throws ApplicationException {
		if (!this.customerDao.isCustomerExistById(customerId)) {
			throw new ApplicationException(ErrorType.CUSTOMER_NOT_FOUND, "Customer with ID " + customerId + " not found");
		}
	}

	public void updateCustomer(Customer customer) throws ApplicationException {
		validateUpdateCustomer(customer.getCustomerId(), customer.getPassword());
		this.customerDao.updateCustomer(customer);
	}

	// validation of the updateCustomer(Customer customer) method
	private void validateUpdateCustomer(long customerId, String password) throws ApplicationException {
		if (!this.customerDao.isCustomerExistById(customerId)) {
			throw new ApplicationException(ErrorType.CUSTOMER_NOT_FOUND, "Customer with ID " + customerId + " not found");
		}
		if (!PasswordUtils.isPasswordValid(password)) {
			throw new ApplicationException(ErrorType.INVALID_PASSWORD, "The password format is invalid.");
		}
	}

	public Customer getCustomer(long customerId) throws ApplicationException {
		Customer customer = this.customerDao.getCustomer(customerId);
		return customer;
	}

	public Collection<Customer> getAllCustomers() throws ApplicationException {
		Collection<Customer> allCustomers = this.customerDao.getAllCustomers();
		return allCustomers;
	}

	public boolean login(String customerName, String password) throws ApplicationException {
		boolean isLoginVerified = this.customerDao.login(customerName, password);
		return isLoginVerified;
	}
	
	public long getCustomerIdByCustomerNameAndPassword(String customerName, String password) throws ApplicationException {
		long customerId = this.customerDao.getCustomerIdByCustomerNameAndPassword(customerName, password);
		return customerId;
	}

}