package com.yairayalon.coupons.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Company {

	private long companyId;
	private String companyName;
	private String password;
	private String email;

	// default constructor
	public Company() {
		super();
	}

	// a constructor which uses only part of the variables (ideal for creating a company)
	public Company(String companyName, String password, String email) {
		super();
		this.companyName = companyName;
		this.password = password;
		this.email = email;
	}

	// a constructor which uses only part of the variables (ideal for updating a company)
	public Company(long companyId, String password, String email) {
		super();
		this.companyId = companyId;
		this.password = password;
		this.email = email;
	}

	// a full constructor
	public Company(long companyId, String companyName, String password, String email) {
		super();
		this.companyId = companyId;
		this.companyName = companyName;
		this.password = password;
		this.email = email;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// toString - used to display company's details
	@Override
	public String toString() {
		return "Company [companyId=" + companyId + ", companyName=" + companyName + ", password=" + password
				+ ", email=" + email + "]";
	}

}