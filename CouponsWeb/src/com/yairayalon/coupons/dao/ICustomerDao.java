package com.yairayalon.coupons.dao;

import java.util.Collection;

import com.yairayalon.coupons.beans.Customer;
import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

// interface to define the operation needed for a Customer object
public interface ICustomerDao {

	public void createCustomer(Customer customer) throws ApplicationException;
	public void removeCustomer(long customerId) throws ApplicationException;
	public void updateCustomer(Customer customer) throws ApplicationException;
	public Customer getCustomer(long customerId) throws ApplicationException;
	public Collection<Customer> getAllCustomers() throws ApplicationException;
	public boolean login(String customerName, String password) throws ApplicationException;

}