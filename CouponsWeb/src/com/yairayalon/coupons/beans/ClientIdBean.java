package com.yairayalon.coupons.beans;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ClientIdBean {
	
	private long clientId;

	public ClientIdBean() {
		super();
	}

	public ClientIdBean(long clientId) {
		super();
		this.clientId = clientId;
	}
	
	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}
	
	@Override
	public String toString() {
		return "ClientIdBean [clientId=" + clientId + "]";
	}
	
}