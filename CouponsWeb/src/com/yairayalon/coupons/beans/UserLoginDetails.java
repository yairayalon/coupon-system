package com.yairayalon.coupons.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserLoginDetails {
	
	private String name;
	private String password;
	private int clientTypeId;

	public UserLoginDetails() {
		super();
	}

	public UserLoginDetails(String name, String password, int clientTypeId) {
		super();
		this.name = name;
		this.password = password;
		this.clientTypeId = clientTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getClientTypeId() {
		return clientTypeId;
	}

	public void setClientTypeId(int clientTypeId) {
		this.clientTypeId = clientTypeId;
	}
	
	@Override
	public String toString() {
		return "UserLoginDetails [name=" + name + ", password=" + password + ", clientTypeId=" + clientTypeId + "]";
	}
	
}