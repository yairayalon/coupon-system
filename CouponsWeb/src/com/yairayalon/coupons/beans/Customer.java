package com.yairayalon.coupons.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Customer {

	private long customerId;
	private String customerName;
	private String password;

	// default constructor
	public Customer() {
		super();
	}

	// a constructor which uses only part of the variables (ideal for creating a customer)
	public Customer(String customerName, String password) {
		super();
		this.customerName = customerName;
		this.password = password;
	}

	// a constructor which uses only part of the variables (ideal for updating a customer)
	public Customer(long customerId, String password) {
		super();
		this.customerId = customerId;
		this.password = password;
	}

	// a full constructor
	public Customer(long customerId, String customerName, String password) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.password = password;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// toString - used to display customer's details
	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", customerName=" + customerName + ", password=" + password + "]";
	}

}